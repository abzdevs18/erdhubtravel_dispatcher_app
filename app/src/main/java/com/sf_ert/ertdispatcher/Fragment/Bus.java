package com.sf_ert.ertdispatcher.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sf_ert.ertdispatcher.Adapter.ActivityAdapter;
import com.sf_ert.ertdispatcher.Adapter.BusAdapter;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.Dialog.SetBusTimeDialog;
import com.sf_ert.ertdispatcher.Model.Alert;
import com.sf_ert.ertdispatcher.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bus extends Fragment {
    View v;
//    LinearLayout mAvailBus;
    RecyclerView mBusAvail;
    private List<com.sf_ert.ertdispatcher.Model.Bus> bus;

    private BusAdapter busAdapter;
    private ApiInterface apiInterface;

    public Bus(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bus,container,false);
        mBusAvail = v.findViewById(R.id.busRecycler);
        LinearLayoutManager transitActivity = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mBusAvail.setLayoutManager(transitActivity);
        mBusAvail.setHasFixedSize(true);

//        mAvailBus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SetBusTimeDialog setBusTimeDialog = new SetBusTimeDialog();
//                setBusTimeDialog.show(getActivity().getSupportFragmentManager(),"Bus Avail");
//            }
//        });

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<com.sf_ert.ertdispatcher.Model.Bus>> call = apiInterface.getBus();
        call.enqueue(new Callback<List<com.sf_ert.ertdispatcher.Model.Bus>>() {
            @Override
            public void onResponse(Call<List<com.sf_ert.ertdispatcher.Model.Bus>> call, Response<List<com.sf_ert.ertdispatcher.Model.Bus>> response) {
                bus = response.body();
                busAdapter = new BusAdapter(bus,getContext());
                mBusAvail.setAdapter(busAdapter);
//                Toast.makeText(getContext(),response.body().size(),Toast.LENGTH_SHORT).show();
                Log.d("D",response.body().get(0).getBodyNum());
            }

            @Override
            public void onFailure(Call<List<com.sf_ert.ertdispatcher.Model.Bus>> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}
