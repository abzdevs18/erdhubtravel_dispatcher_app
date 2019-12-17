package com.sf_ert.ertdispatcher.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Route {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("from_terminal")
    private String from_terminal;

    @SerializedName("to_terminal")
    private String to_terminal;

    public Route(String id, String name, String from_terminal, String to_terminal) {
        this.id = id;
        this.name = name;
        this.from_terminal = from_terminal;
        this.to_terminal = to_terminal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFrom_terminal(String from_terminal) {
        this.from_terminal = from_terminal;
    }

    public void setTo_terminal(String to_terminal) {
        this.to_terminal = to_terminal;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFrom_terminal() {
        return from_terminal;
    }

    public String getTo_terminal() {
        return to_terminal;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
