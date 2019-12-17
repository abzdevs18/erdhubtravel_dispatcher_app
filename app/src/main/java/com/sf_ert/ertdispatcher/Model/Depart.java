package com.sf_ert.ertdispatcher.Model;

import com.google.gson.annotations.SerializedName;

public class Depart {
    @SerializedName("code")
    private String code;
    @SerializedName("route")
    private String route;
    @SerializedName("route_id")
    private String route_id;

    public String getRoute() {
        return route;
    }

    public String getCode() {
        return code;
    }

    public String getRoute_id() {
        return route_id;
    }
}
