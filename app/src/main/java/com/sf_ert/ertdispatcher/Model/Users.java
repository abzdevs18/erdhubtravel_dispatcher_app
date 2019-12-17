package com.sf_ert.ertdispatcher.Model;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("p_k")
    private String p_k;

    @SerializedName("bus_num")
    private String bus_num;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("lastname")
    private String lastname;

    public String getP_k() {
        return p_k;
    }

    public String getBus_num() {
        return bus_num;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }
}
