package com.example.unsplash.unsplash;

import com.example.unsplash.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UnsplashAPI {

    @GET("/photos")
    Call<List<Photo>> getPhotos();
}