package com.fruits.vlk.fest.api.network.apiClients;


import com.fruits.vlk.fest.api.network.apiServises.ApiServiceSmsGorod;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClientSmsGorod {

    private static ApiClientSmsGorod mInstance;
    private static final String BASE_URL = "https://smsbuilder.ru/api/";
    private final Retrofit mRetrofit;

    private ApiClientSmsGorod(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static ApiClientSmsGorod getInstance(){
        if(mInstance == null) {
            mInstance = new ApiClientSmsGorod();
        }
        return mInstance;
    }

    public ApiServiceSmsGorod getApiServiceSmsGorod(){
        return mRetrofit.create(ApiServiceSmsGorod.class);
    }

}
