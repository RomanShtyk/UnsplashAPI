package com.example.unsplash.model.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.unsplash.model.models.Photo
import com.example.unsplash.model.unsplash.Unsplash
import com.example.unsplash.model.unsplash.UnsplashAPI
import com.example.unsplash.view.MainActivity.Companion.token
import com.example.unsplash.view.MainActivity.Companion.username
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouritesDataSource : PageKeyedDataSource<Int, Photo>() {
    private val CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d"
    private val unsplashAPI =
        Unsplash.getRetrofitTokenedAPIInstance(token)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        unsplashAPI.getUserLikes(username, FIRST_PAGE).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.body() != null) {
                    callback.onResult(response.body()!!, null, FIRST_PAGE + 1)
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.d("mLog", "onFailure: PhotoDataSource")
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        unsplashAPI.getUserLikes(username, params.key).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.body() != null) {
                    val key = if (params.key > 1) params.key - 1 else null
                    callback.onResult(response.body()!!, key)
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.d("mLog", "onFailure: PhotoDataSource loadBefore")
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        unsplashAPI.getUserLikes(username, params.key).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.body() != null) {
                    val key = params.key + 1
                    callback.onResult(response.body()!!, key)
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.d("mLog", "onFailure: PhotoDataSource loadAfter")
            }
        })
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
