package com.example.unsplash.model.unsplash;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Unsplash {
    private static final String BASE_URL = "https://api.unsplash.com/";
    public static final String BASE_URL_POST = "https://unsplash.com/";
    public static final String SECRET = "b58b90999ac3418998afd075025bfe1541ccb15d60703ee945e8c5846a95862d";
    public static final String REDIRECT_URI = "https://myclient";
    public static final String CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d";


    public static Retrofit getRetrofitInstance(String clientId) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(clientId)).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofitPostInstance(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptorPost(token)).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
