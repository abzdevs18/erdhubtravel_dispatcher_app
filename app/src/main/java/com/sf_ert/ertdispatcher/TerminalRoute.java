package com.sf_ert.ertdispatcher;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.Fragment.Account;
import com.sf_ert.ertdispatcher.Fragment.Bus;
import com.sf_ert.ertdispatcher.Fragment.ChatList;
import com.sf_ert.ertdispatcher.Fragment.Home;
import com.sf_ert.ertdispatcher.Fragment.Messenger;
import com.sf_ert.ertdispatcher.Fragment.Schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class TerminalRoute extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

//    List<Terminal> terminals;
    private ApiInterface apiInterface;



    MapView mapView;

    private MapboxMap map;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button button;
    // Animator
    private ValueAnimator markerAnimator;
    private boolean markerSelected = false;
//    RelativeLayout mHomeLayout;
//    int Navigation = 0;

//    MaterialSearchView searchView;
    Toolbar toolbar;
    Fragment fragment;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String MARKER_IMAGE = "custom-image";
    private static final String LAYER_ID = "LAYER_ID";



//    Checking functionality BUS ID;

    private String BUS_ID = "";

    private static Double NEW_LAT_POINT_ONE;
    private static Double NEW_LONG_POINT_ONE;

    private static Double NEW_LAT_POINT_TWO;
    private static Double NEW_LONG_POINT_TWO;

    //    Socket
    private static Socket mSocket;

//    State of the Location
    private Boolean located = false;
    private Marker markerView;

    {
        try {
            mSocket = IO.socket("http://erdhubtravel.ga:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        // This contains the MapView in XML and needs to be called after the access token is configured.
//        setContentView(R.layout.activity_terminal_route);
        setContentView(R.layout.activity_terminal_route);

//        String[] partOne = pointOne.split(", ");
//        NEW_LONG_POINT_ONE = Double.parseDouble(partOne[0]);
//        NEW_LAT_POINT_ONE = Double.parseDouble(partOne[1]);
//
//        String[] partTwo = pointTwo.split(", ");
//        NEW_LONG_POINT_TWO = Double.parseDouble(partTwo[0]);
//        NEW_LAT_POINT_TWO = Double.parseDouble(partTwo[1]);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        mapView = findViewById(R.id.mapViewTerminal);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

//      websockets
        mSocket.on("message", onNewMessage); //we set this if we are trying to Listen the events of the socket
        mSocket.connect();

        BUS_ID = getIntent().getExtras().getString("BUS_ID");

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String bus_id;
                    String latitude;
                    String longitude;
                    String pointOne;
                    String pointTwo;

                    try {
                        bus_id = data.getString("bus_id");
                        latitude = data.getString("latitude");
                        longitude = data.getString("longitude");
                        pointOne = data.getString("to_route");
                        pointTwo = data.getString("from_route");

                        String[] partOne = pointOne.split(", ");
                        NEW_LONG_POINT_ONE = Double.parseDouble(partOne[0]);
                        NEW_LAT_POINT_ONE = Double.parseDouble(partOne[1]);

                        String[] partTwo = pointTwo.split(", ");
                        NEW_LONG_POINT_TWO = Double.parseDouble(partTwo[0]);
                        NEW_LAT_POINT_TWO = Double.parseDouble(partTwo[1]);


                    } catch (JSONException e) {
                        return;
                    }

//                    Marker Setting Icon Resource;
                    IconFactory iconFactory = IconFactory.getInstance(TerminalRoute.this);
//                    Drawable icon = ContextCompat.getDrawable(TerminalRoute.this, R.drawable.busstop);
                    Icon i = iconFactory.fromResource(R.drawable.busstop);

                    if (bus_id.equals(BUS_ID)){

                        Point destinationPoint = Point.fromLngLat(NEW_LONG_POINT_ONE, NEW_LAT_POINT_ONE);
                        Point originPoint = Point.fromLngLat(NEW_LONG_POINT_TWO, NEW_LAT_POINT_TWO);

                        getRoute(originPoint,destinationPoint);


                        if(located){
                            map.removeMarker(markerView);
                            markerView = map.addMarker(new MarkerOptions()
                                    .setIcon(i)
                                    .position(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude))));

                        }else{
                            located = true;
                            markerView = map.addMarker(new MarkerOptions()
                                    .setIcon(i)
                                    .position(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude))));
                        }

                    }
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            String tag = "";
            switch (menuItem.getItemId()){
                case R.id.home:
                    fragment = new Home();
                    tag = "Home";
                    break;
                case R.id.bus:
                    fragment = new Bus();
                    tag = "Bus";
                    break;
                case R.id.schedule:
                    fragment = new Schedule();
                    tag = "Schedule";
                    break;
                case R.id.message:
                    fragment = new ChatList();
                    tag = "Chat";
                    break;
                default:
                    break;
            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,selectedFragment).commit();
//            ActionBar actionBar = getSupportActionBar();
//            actionBar.setElevation(0);
            return true;
        }
    };

    @SuppressWarnings( {"MissingPermission"})
    public void enableLocation(@NonNull Style loadedMapStyle){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine();
            initializeLocationLayer();

        }else{
            permissionsManager =  new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
//        loadedMapStyle.addImage("destination-icon-id",
//                BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_location_on_black_24dp));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        Style style = map.getStyle();

        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

//        GeoJsonSource source = map.getStyle().getSourceAs("destination-source-id");
//        if (source != null) {
//            source.setGeoJson(Feature.fromGeometry(destinationPoint));
//        }

        getRoute(originPoint, destinationPoint);
        button.setEnabled(true);
//        button.setBackgroundResource(R.color.mapboxBlue);

        if (style != null) {
            final SymbolLayer selectedMarkerSymbolLayer =
                    (SymbolLayer) style.getLayer("selected-marker-layer");

            final PointF pixel = map.getProjection().toScreenLocation(point);
            List<Feature> features = map.queryRenderedFeatures(pixel, "marker-layer");
            List<Feature> selectedFeature = map.queryRenderedFeatures(
                    pixel, "selected-marker-layer");

            if (selectedFeature.size() > 0 && markerSelected) {
                return false;
            }
            GeoJsonSource source = style.getSourceAs("selected-marker");
            if (source != null) {
                source.setGeoJson(FeatureCollection.fromFeatures(
                        new Feature[]{Feature.fromGeometry(features.get(0).geometry())}));
            }
        }
        return true;
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    private void initializeLocationEngine() {
    }

    private void initializeLocationLayer() {
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.map = mapboxMap;


        List<Feature> symbolicLayerIconList = new ArrayList<>();
//
//        String latlng = response.body().get(i).getLatlong();
//        String[] latlngpart = latlng.split(", ");
//        Double snapLat = Double.parseDouble(latlngpart[0]);
//        Double snapLng = Double.parseDouble(latlngpart[1]);


        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Point destinationPoint = Point.fromLngLat(marker.getPosition().getLongitude(),marker.getPosition().getLatitude());
                Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                        locationComponent.getLastKnownLocation().getLatitude());
                getRoute(originPoint,destinationPoint);
                return true;
            }
        });

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocation(style);
                        addDestinationIconSymbolLayer(style);
//                        terminalLocations();

//                        mapboxMap.addOnMapClickListener(TerminalRoute.this);
//                        Last working ends here


                        // Add the marker image to map
                        style.addSource(new GeoJsonSource("marker-source",
                                FeatureCollection.fromFeatures(symbolicLayerIconList)));

//                        style.addImage(MARKER_IMAGE, BitmapFactory.decodeResource(TerminalRoute.this.getResources(), R.drawable.terminal));
                        style.addImage(MARKER_IMAGE,TerminalRoute.this.getResources().getDrawable(R.drawable.busstop));


                        // Adding an offset so that the bottom of the blue icon gets fixed to the coordinate, rather than the
                        // middle of the icon being fixed to the coordinate point.
                        style.addLayer(new SymbolLayer("marker-layer", "marker-source")
                                .withProperties(PropertyFactory.iconImage("my-marker-image"),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f, -9f})));

                        // Add the selected marker source and layer
                        style.addSource(new GeoJsonSource("selected-marker"));

                        // Adding an offset so that the bottom of the blue icon gets fixed to the coordinate, rather than the
                        // middle of the icon being fixed to the coordinate point.
                        style.addLayer(new SymbolLayer("selected-marker-layer", "selected-marker")
                                .withProperties(PropertyFactory.iconImage(MARKER_IMAGE),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f, -9f})));

//                        This is just a hacked part code. This will disable clicks in the part of the map. so the user will only be allowed to
//                        click on the markers.

//                        mapboxMap.addOnMapClickListener(TerminalRoute.this);
                    }
                });
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation(map.getStyle());
        } else {
            Toast.makeText(this, "Nor", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("message", onNewMessage);
        mapView.onDestroy();
    }
//
//    @Override
//    public boolean onMapClick(@NonNull LatLng point) {
//        return false;
//    }
}
