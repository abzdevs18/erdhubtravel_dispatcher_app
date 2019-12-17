package com.sf_ert.ertdispatcher.Model;

import com.google.gson.annotations.SerializedName;

/*
* This class is for the Transit Alert
* whenever the Easy-Ride Depart from the
* Terminal, This is the model of the event
* */
public class Alert {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("dpart")
    private String dpart;

    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDpart() {
        return dpart;
    }

    public String getStatus() {
        return status;
    }
}
