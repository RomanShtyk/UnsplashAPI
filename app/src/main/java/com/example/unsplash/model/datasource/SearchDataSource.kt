package com.example.unsplash.model.datasource

import androidx.paging.PageKeyedDataSource
import com.example.unsplash.model.models.Photo
import com.example.unsplash.model.models.SearchResults
import com.example.unsplash.model.unsplash.Unsplash
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDataSource internal constructor(private val query: String) :
    PageKeyedDataSource<Int, Photo>() {
    private val unsplashAPI =
        Unsplash.getRetrofitAPIInstance(false)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        unsplashAPI.searchPhotos(query, FIRST_PAGE).enqueue(object : Callback<SearchResults> {
            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                if (response.body() != null) {
                    callback.onResult(response.body()!!.results!!, null, FIRST_PAGE + 1)
                }
            }

            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        unsplashAPI.searchPhotos(query, params.key).enqueue(object : Callback<SearchResults> {
            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                if (response.body() != null) {
                    val key = if (params.key > 1) params.key - 1 else null
                    callback.onResult(response.body()!!.results!!, key)
                }
            }

            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        unsplashAPI.searchPhotos(query, params.key).enqueue(object : Callback<SearchResults> {
            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                if (response.body() != null) {
                    val key = params.key + 1
                    callback.onResult(response.body()!!.results!!, key)
                }
            }

            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
            }
        })
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
