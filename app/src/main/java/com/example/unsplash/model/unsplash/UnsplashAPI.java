package com.example.unsplash.model.unsplash;

import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.model.models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnsplashAPI {

    @GET("/photos")
    Call<List<Photo>> getPhotos(@Query("page") int page);

    @GET("/search/photos")
    Call<SearchResults> searchPhotos(@Query("query") String query, @Query("page") int page);

    @GET("/collections")
    Call<List<Collection>> getCollections(@Query("page") int page);
}