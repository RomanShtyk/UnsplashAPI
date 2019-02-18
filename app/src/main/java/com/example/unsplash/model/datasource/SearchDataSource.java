package com.example.unsplash.model.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.example.unsplash.model.models.Photo;
import com.example.unsplash.model.models.SearchResults;
import com.example.unsplash.model.unsplash.Unsplash;
import com.example.unsplash.model.unsplash.UnsplashAPI;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDataSource extends PageKeyedDataSource<Integer, Photo> {
    private static final int FIRST_PAGE = 1;
    private final String CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d";
    private UnsplashAPI unsplashAPI = Unsplash.getRetrofitInstance(CLIENT_ID).create(UnsplashAPI.class);
    private String query;

    SearchDataSource(String query) {
        this.query = query;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Photo> callback) {
        unsplashAPI.searchPhotos(query, FIRST_PAGE).enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(@NonNull Call<SearchResults> call, @NonNull Response<SearchResults> response) {
                if (response.body() != null) {
                    callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResults> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        unsplashAPI.searchPhotos(query, params.key).enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(@NonNull Call<SearchResults> call, @NonNull Response<SearchResults> response) {
                if (response.body() != null) {
                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.body().getResults(), key);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResults> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void loadAfter
            (@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {
        unsplashAPI.searchPhotos(query, FIRST_PAGE).enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(@NonNull Call<SearchResults> call, @NonNull Response<SearchResults> response) {
                if (response.body() != null) {
                    Integer key = params.key + 1;
                    callback.onResult(response.body().getResults(), key);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResults> call, @NonNull Throwable t) {

            }
        });
    }
}
