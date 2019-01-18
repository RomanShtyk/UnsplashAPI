package com.example.unsplash.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.unsplash.models.Photo;
import com.example.unsplash.unsplash.Unsplash;
import com.example.unsplash.unsplash.UnsplashAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("NullableProblems")
public class MainActivityRepo {
    private final String CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d";
    private UnsplashAPI unsplashAPI = Unsplash.getRetrofitInstance(CLIENT_ID).create(UnsplashAPI.class);
    private MutableLiveData<List<Photo>> list = new MutableLiveData<>();

    public MutableLiveData<List<Photo>> getPhoto() {
        unsplashAPI.getPhotos().enqueue(new Callback<List<Photo>>() {

            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                list.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.d("mLog", t.toString());
            }

        });
        return list;
    }


}
