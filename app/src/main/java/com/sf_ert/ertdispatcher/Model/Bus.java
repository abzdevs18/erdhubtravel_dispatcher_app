package com.sf_ert.ertdispatcher.Model;

import com.google.gson.annotations.SerializedName;

public class Bus {
    @SerializedName("busId")
    private String busId;

    @SerializedName("bodyNum")
    private String bodyNum;

    @SerializedName("driver")
    private String driver;

    public String getBusId() {
        return busId;
    }

    public String getBodyNum() {
        return bodyNum;
    }

    public String getDriver() {
        return driver;
    }
}
