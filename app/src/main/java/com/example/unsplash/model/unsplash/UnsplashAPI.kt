package com.example.unsplash.model.unsplash

import com.example.unsplash.model.models.*
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashAPI {

    @get:GET("/me")
    val meProfile: Call<Me>

    @GET("/photos")
    fun getPhotos(@Query("page") page: Int): Call<List<Photo>>

    @GET("/search/photos")
    fun searchPhotos(@Query("query") query: String, @Query("page") page: Int): Call<SearchResults>

    @GET("/collections")
    fun getCollections(@Query("page") page: Int): Call<List<ColletionPhotos>>

    @GET("/collections/{id}/photos")
    fun getCollectionPhotos(@Path("id") id: String, @Query("page") page: Int): Call<List<Photo>>

    @GET("photos/{id}")
    fun getPhoto(@Path("id") id: String): Call<Photo>

    @POST("photos/{id}/like")
    fun likeAPhoto(@Path("id") id: String): Call<LikePhotoResult>

    @DELETE("photos/{id}/like")
    fun dislikeAPhoto(@Path("id") id: String): Call<LikePhotoResult>

    @POST("/oauth/token")
    fun getAccessToken(
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("redirect_uri") redirect_uri: String,
        @Query("code") code: String,
        @Query("grant_type") grant_type: String
    ): Call<AccessToken>

    @GET("/users/{username}/likes")
    fun getUserLikes(
        @Path("username") username: String,
        @Query("page") page: Int?
    ): Call<List<Photo>>
}
