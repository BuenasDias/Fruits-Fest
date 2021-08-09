package com.fruits.vlk.fest.api.network.apiServises;

import com.fruits.vlk.fest.api.requests.auth.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceSmsGorod {

    @GET("create")
    Call<Response> getSmsCode(@Query("phone") String userPhone,
                              @Query("key") String apiKey);


    @GET("create")
    Call<Response> getSmsCodeTest(@Query("phone") String userPhone,
                                  @Query("key") String apiKey,
                                  @Query("test") boolean boolTest);


}
