package com.example.unsplash.model.datasource

import androidx.paging.PageKeyedDataSource
import com.example.unsplash.model.models.Collection
import com.example.unsplash.model.unsplash.Unsplash
import com.example.unsplash.model.unsplash.UnsplashAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionDataSource : PageKeyedDataSource<Int, Collection>() {
    private val CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d"


    private val unsplashAPI = Unsplash.getRetrofitInstance(CLIENT_ID).create(UnsplashAPI::class.java)

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Collection>) {
        unsplashAPI.getCollections(FIRST_PAGE).enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                if (response.body() != null) {
                    callback.onResult(response.body()!!, null, FIRST_PAGE + 1)
                }
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {

            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Collection>) {
        unsplashAPI.getCollections(params.key).enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                if (response.body() != null) {
                    val key = if (params.key > 1) params.key - 1 else null
                    callback.onResult(response.body()!!, key)
                }
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {

            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Collection>) {
        unsplashAPI.getCollections(params.key).enqueue(object : Callback<List<Collection>> {
            override fun onResponse(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                if (response.body() != null) {
                    val key = params.key + 1
                    callback.onResult(response.body()!!, key)
                }
            }

            override fun onFailure(call: Call<List<Collection>>, t: Throwable) {

            }
        })
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
