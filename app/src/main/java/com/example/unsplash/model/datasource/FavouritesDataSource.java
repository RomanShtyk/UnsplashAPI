package com.example.unsplash.model.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.unsplash.model.models.Photo;
import com.example.unsplash.model.unsplash.Unsplash;
import com.example.unsplash.model.unsplash.UnsplashAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.unsplash.view.MainActivity.token;
import static com.example.unsplash.view.MainActivity.username;

public class FavouritesDataSource extends PageKeyedDataSource<Integer, Photo> {

    private static final int FIRST_PAGE = 1;
    private final String CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d";
    private UnsplashAPI unsplashAPI = Unsplash.getRetrofitPostInstance(token).create(UnsplashAPI.class);


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Photo> callback) {
        unsplashAPI.getUserLikes(username, FIRST_PAGE).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Photo>> call, @NonNull Response<List<Photo>> response) {
                if (response.body() != null) {
                    callback.onResult(response.body(), null, FIRST_PAGE + 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t) {
                Log.d("mLog", "onFailure: PhotoDataSource");
            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        unsplashAPI.getUserLikes(username,params.key).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Photo>> call, @NonNull Response<List<Photo>> response) {
                if (response.body() != null) {
                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.body(), key);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t) {
                Log.d("mLog", "onFailure: PhotoDataSource loadBefore");
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        unsplashAPI.getUserLikes(username,params.key).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Photo>> call, @NonNull Response<List<Photo>> response) {
                if (response.body() != null) {
                    Integer key = params.key + 1;
                    callback.onResult(response.body(), key);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t) {
                Log.d("mLog", "onFailure: PhotoDataSource loadAfter");
            }
        });
    }
}