package com.sf_ert.ertdispatcher.Dialog;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.MainActivity;
import com.sf_ert.ertdispatcher.Model.Bus;
import com.sf_ert.ertdispatcher.Model.Route;
import com.sf_ert.ertdispatcher.Model.Schedule;
import com.sf_ert.ertdispatcher.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetBusTimeDialog extends BottomSheetDialogFragment {

    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPM;

    Button mSelect;
    TextView mTimeSelected, mSave;

    Spinner mSpinner;
    String busId,routeId;
    String routeName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.bottom_sheet_layout,container,false);

        mTimeSelected = v.findViewById(R.id.timeSelected);
        mSpinner = v.findViewById(R.id.spinner);

        mSelect = v.findViewById(R.id.selectTime);
        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int pickH = hourOfDay;
                        if (pickH > 12 || pickH >= 12){
                            pickH = hourOfDay - 12;
                            amPM = "PM";
                        }else{
                            amPM = "AM";
                        }
                        mTimeSelected.setText(String.format("%02d:%02d",pickH,minute) + amPM);
                    }
                },currentHour,currentMinute,false);

                timePickerDialog.show();

            }
        });

        final List<Route> routeList = new ArrayList<>();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Route>> call = apiInterface.getSpinnerRoute();
        call.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                for (int i = 0; i < response.body().size(); i++){
                    Route route = new Route(response.body().get(i).getId(),response.body().get(i).getName(),response.body().get(i).getFrom_terminal(), response.body().get(i).getTo_terminal());
                    routeList.add(route);

                    Log.d("TAG",response.body().get(i).getName());
                }
                ArrayAdapter<Route> adapter = new ArrayAdapter<Route>(getContext(),
                        android.R.layout.simple_spinner_item,routeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {

            }
        });
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                routeName = parent.getItemAtPosition(position).toString();

//                Toast.makeText(getActivity(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextView busId = v.findViewById(R.id.mBus);
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Saving");

        mSave = v.findViewById(R.id.mSaveSchedule);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String id = busId.getText().toString();
                pd.show();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bus", Context.MODE_PRIVATE);
                final String bId = sharedPreferences.getString("bId","");
                String busNum = bId;
                String routeTime = mTimeSelected.getText().toString();
                String routeN = routeName;
                Log.d("T",busNum + routeTime + routeN);


                ApiInterface api = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Schedule> busSched = api.setSched(busNum,routeTime,routeN);
                busSched.enqueue(new Callback<Schedule>() {
                    @Override
                    public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        Toast.makeText(getContext(),"sched added",Toast.LENGTH_SHORT).show();
                        pd.hide();
                    }

                    @Override
                    public void onFailure(Call<Schedule> call, Throwable t) {

                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        pd.hide();
                    }
                });

            }
        });



        return v;
    }

    public void getSelected(View v){
        Route route = (Route)mSpinner.getSelectedItem();
    }
}
