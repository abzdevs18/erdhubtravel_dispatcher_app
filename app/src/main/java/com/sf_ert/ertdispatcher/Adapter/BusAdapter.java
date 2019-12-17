package com.sf_ert.ertdispatcher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sf_ert.ertdispatcher.Dialog.SetBusTimeDialog;
import com.sf_ert.ertdispatcher.Login;
import com.sf_ert.ertdispatcher.Model.Alert;
import com.sf_ert.ertdispatcher.Model.Bus;
import com.sf_ert.ertdispatcher.R;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {
    List<Bus> alert;
    Context context;

    public BusAdapter(List<Bus> alert, Context context) {
        this.alert = alert;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.avail_bus_item,viewGroup,false);
        return new BusAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.mBody.setText(alert.get(i).getBodyNum());
        myViewHolder.mDriver.setText(alert.get(i).getDriver());
        myViewHolder.mId.setText(alert.get(i).getBusId());
        myViewHolder.mContext  = this.context;
        Bus bus = alert.get(i);
    }

    @Override
    public int getItemCount() {
        return alert.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mBody,mDriver,mId;
        Context mContext;
        FragmentManager fm;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            Typeface gibsonRegular = Typeface.createFromAsset(context.getAssets(),"fonts/Gibson-Regular.ttf");
            mBody = (TextView)itemView.findViewById(R.id.bodyNum);
            mId = itemView.findViewById(R.id.mBus);
//            mBody.setTypeface(gibsonRegular);

            mDriver = (TextView)itemView.findViewById(R.id.driverName);
//            mDriver.setTypeface(gibsonRegular);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences sharedPref = view.getContext().getSharedPreferences("bus", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("bId",mId.getText().toString());
                    editor.apply();

                SetBusTimeDialog setBusTimeDialog = new SetBusTimeDialog();
                setBusTimeDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(),"Avail Bus");
//                    Toast.makeText(view.getContext(),mId.getText().toString(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
