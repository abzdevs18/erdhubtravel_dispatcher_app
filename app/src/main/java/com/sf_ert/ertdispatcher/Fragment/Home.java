package com.sf_ert.ertdispatcher.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sf_ert.ertdispatcher.Adapter.ActivityAdapter;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.Model.Alert;
import com.sf_ert.ertdispatcher.Model.Terminal;
import com.sf_ert.ertdispatcher.R;
import com.sf_ert.ertdispatcher.TerminalRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {
    private List<Alert> alerts;

    private ActivityAdapter alertAdapter;
    private ApiInterface apiInterface;

    private RecyclerView mTransitActivity;
    View v;
    public Home(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home,container,false);

        mTransitActivity = v.findViewById(R.id.activityRecycler);
        LinearLayoutManager transitActivity = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mTransitActivity.setLayoutManager(transitActivity);
        mTransitActivity.setHasFixedSize(true);

        LinearLayout button = v.findViewById(R.id.checkLoc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TerminalRoute.class);
                intent.putExtra("BUS_ID", "2");
                startActivity(intent);
            }
        });
        return v;
    }

    public void alert() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<List<Alert>> transitAlert = apiInterface.transitActivity();
        transitAlert.enqueue(new Callback<List<Alert>>() {
            @Override
            public void onResponse(Call<List<Alert>> call, Response<List<Alert>> response) {
                alerts = response.body();
                alertAdapter = new ActivityAdapter(alerts,getActivity());
                mTransitActivity.setAdapter(alertAdapter);

            }

            @Override
            public void onFailure(Call<List<Alert>> call, Throwable t) {

            }
        });
    }
}
