package com.fruits.vlk.fest.api.network.apiServises;

import com.fruits.vlk.fest.api.requests.authInfo.ResponseAuthInfo;
import com.fruits.vlk.fest.api.requests.checker.Response;
import com.fruits.vlk.fest.api.requests.newProducts.ResponseProducts;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceMagicChecker {

    @GET("index.php")
    Call<Response> getCheckerContent();

    // TODO починить запрос
    @GET("new-products.html")
    Call<ResponseProducts> getProducts();

    @GET("auth-info.html")
    Call<ResponseAuthInfo> getAuthInfo();
}
