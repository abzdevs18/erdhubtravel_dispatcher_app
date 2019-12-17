package com.sf_ert.ertdispatcher.Model;

import com.google.gson.annotations.SerializedName;

public class Row {
    // TODO: 10/25/2019 Used for login response

    @SerializedName("fId")
    private String fId;

    @SerializedName("usr_id")
    private String usr_id;

    @SerializedName("usrEmail")
    private String usrEmail;

    @SerializedName("usrName")
    private String usrName;

    @SerializedName("terminalName")
    private String terminalName;

    @SerializedName("uType")
    private String uType;

    public String getfId() {
        return fId;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    public String getUsrName() {
        return usrName;
    }

    public String getuType() {
        return uType;
    }
}
