package com.example.unsplash.model.unsplash

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Unsplash {
    private const val BASE_URL = "https://api.unsplash.com/"
    private const val BASE_URL_POST = "https://unsplash.com/"
    const val SECRET = "b58b90999ac3418998afd075025bfe1541ccb15d60703ee945e8c5846a95862d"
    const val REDIRECT_URI = "myapp://my"
    const val CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d"
    const val UNSPLASH_UPLOAD_URL = "https://unsplash.com/submit"
    const val UNSPLASH_LOGIN_URI =
        "https://unsplash.com/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&response_type=code&scope=public+write_likes+read_user"

    fun getRetrofitAPIInstance(isLogin: Boolean): UnsplashAPI {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(CLIENT_ID)).build()
        return Retrofit.Builder()
            .baseUrl(if (isLogin) BASE_URL_POST else BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashAPI::class.java)
    }

    fun getRetrofitTokenedAPIInstance(token: String): UnsplashAPI {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptorPost(token)).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashAPI::class.java)
    }
}
