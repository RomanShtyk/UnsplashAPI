package com.example.unsplash.model.unsplash;

import com.example.unsplash.model.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnsplashAPI {

    @GET("/photos")
    Call<List<Photo>> getPhotos(@Query("page") int page);
}