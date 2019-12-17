package com.sf_ert.ertdispatcher.Model;

import com.google.gson.annotations.SerializedName;

public class Schedule {
    @SerializedName("status")
    private String status;
    @SerializedName("busNum")
    private String busNum;
    @SerializedName("departTime")
    private String departTime;
    @SerializedName("busRoute")
    private String busRoute;

    public String getStatus() {
        return status;
    }

    public String getBusNum() {
        return busNum;
    }

    public String getDepartTime() {
        return departTime;
    }

    public String getBusRoute() {
        return busRoute;
    }
}
