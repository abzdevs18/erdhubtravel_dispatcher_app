package com.sf_ert.ertdispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;
import com.sf_ert.ertdispatcher.Fragment.Account;
import com.sf_ert.ertdispatcher.Fragment.Bus;
import com.sf_ert.ertdispatcher.Fragment.ChatList;
import com.sf_ert.ertdispatcher.Fragment.Home;
import com.sf_ert.ertdispatcher.Fragment.Messenger;
import com.sf_ert.ertdispatcher.Fragment.Schedule;

public class MainActivity extends AppCompatActivity {
    RelativeLayout mHomeLayout;
    int Navigation = 0;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        fragment = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,fragment,"Home").addToBackStack("home").commit();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,fragment,tag).addToBackStack("home").commit();
            return true;
        }
    };
}
