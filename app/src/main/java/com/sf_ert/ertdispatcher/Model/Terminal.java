package com.sf_ert.ertdispatcher.Model;

import com.google.gson.annotations.SerializedName;

public class Terminal {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("latlong")
    private String latlong;

    @SerializedName("coordinate_mobile")
    private String coordinate_mobile;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLatlong() {
        return latlong;
    }

    public String getCoordinate_mobile() {
        return coordinate_mobile;
    }
}
