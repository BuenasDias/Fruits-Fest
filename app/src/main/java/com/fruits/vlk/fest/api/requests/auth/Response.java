package com.fruits.vlk.fest.api.requests.auth;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }
}
