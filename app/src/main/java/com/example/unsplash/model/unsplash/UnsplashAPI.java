package com.example.unsplash.model.unsplash;

import com.example.unsplash.model.models.AccessToken;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.LikePhotoResult;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.model.models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnsplashAPI {

    @GET("/photos")
    Call<List<Photo>> getPhotos(@Query("page") int page);

    @GET("/search/photos")
    Call<SearchResults> searchPhotos(@Query("query") String query, @Query("page") int page);

    @GET("/collections")
    Call<List<Collection>> getCollections(@Query("page") int page);

    @GET("/collections/{id}/photos")
    Call<List<Photo>> getCollectionPhotos(@Path("id") String id, @Query("page") int page);

    @POST("photos/{id}/like")
    Call<LikePhotoResult> likeAPhoto(@Path("id") String id);

    @DELETE("photos/{id}/like")
    Call<LikePhotoResult> dislikeAPhoto(@Path("id") String id);

    @POST("/oauth/token")
    Call<AccessToken> getAccessToken(@Query("client_id") String client_id,
                                     @Query("client_secret") String client_secret,
                                     @Query("redirect_uri") String redirect_uri,
                                     @Query("code") String code,
                                     @Query("grant_type") String grant_type);
}