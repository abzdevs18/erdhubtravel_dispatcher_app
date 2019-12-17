package com.sf_ert.ertdispatcher.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sf_ert.ertdispatcher.Model.Alert;
import com.sf_ert.ertdispatcher.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {
    List<Alert> alert;
    Context context;

    public ActivityAdapter(List<Alert> alert, Context context) {
        this.alert = alert;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transit_activity,viewGroup,false);
        return new ActivityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.mRoute.setText(alert.get(i).getName());
        myViewHolder.mTime.setText(alert.get(i).getDpart());
    }

    @Override
    public int getItemCount() {
        return alert.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTime,mRoute,notif_fot,from;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Typeface gibsonRegular = Typeface.createFromAsset(context.getAssets(),"fonts/Gibson-Regular.ttf");
            mTime = (TextView)itemView.findViewById(R.id.depart_time);
            mTime.setTypeface(gibsonRegular);

            mRoute = (TextView)itemView.findViewById(R.id.route);
            mRoute.setTypeface(gibsonRegular);
        }
    }
}
