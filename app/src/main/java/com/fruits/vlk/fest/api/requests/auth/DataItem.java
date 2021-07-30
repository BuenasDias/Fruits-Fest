package com.fruits.vlk.fest.api.requests.auth;

import com.google.gson.annotations.SerializedName;

public class DataItem {
    @SerializedName("createdAt")
    private int createdAt;

    @SerializedName("phone")
    private String phone;

    @SerializedName("id")
    private int id;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public int getCreatedAt(){
        return createdAt;
    }

    public String getPhone(){
        return phone;
    }

    public int getId(){
        return id;
    }

    public String getMessage(){
        return message;
    }

    public String getStatus(){
        return status;
    }
}
