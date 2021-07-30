package com.fruits.vlk.fest.api.network.apiServises;

import com.fruits.vlk.fest.api.requests.checker.Response;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceMagicChecker {

    @GET("index.php")
    Call<Response> getCheckerContent();

    @GET("smsgorod.html")
    Call<com.fruits.vlk.fest.api.requests.smsGorodKey.Response> getApiKeySms();
}
