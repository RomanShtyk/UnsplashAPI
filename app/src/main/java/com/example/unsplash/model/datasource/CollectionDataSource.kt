package com.example.unsplash.model.datasource

import androidx.paging.PageKeyedDataSource
import com.example.unsplash.model.models.ColletionPhotos
import com.example.unsplash.model.unsplash.Unsplash
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionDataSource : PageKeyedDataSource<Int, ColletionPhotos>() {

    private val unsplashAPI =
        Unsplash.getRetrofitAPIInstance(false)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ColletionPhotos>
    ) {
        unsplashAPI.getCollections(FIRST_PAGE).enqueue(object : Callback<List<ColletionPhotos>> {
            override fun onResponse(
                call: Call<List<ColletionPhotos>>,
                response: Response<List<ColletionPhotos>>
            ) {
                if (response.body() != null) {
                    callback.onResult(response.body()!!, null, FIRST_PAGE + 1)
                }
            }

            override fun onFailure(call: Call<List<ColletionPhotos>>, t: Throwable) {
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ColletionPhotos>) {
        unsplashAPI.getCollections(params.key).enqueue(object : Callback<List<ColletionPhotos>> {
            override fun onResponse(
                call: Call<List<ColletionPhotos>>,
                response: Response<List<ColletionPhotos>>
            ) {
                if (response.body() != null) {
                    val key = if (params.key > 1) params.key - 1 else null
                    callback.onResult(response.body()!!, key)
                }
            }

            override fun onFailure(call: Call<List<ColletionPhotos>>, t: Throwable) {
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ColletionPhotos>) {
        unsplashAPI.getCollections(params.key).enqueue(object : Callback<List<ColletionPhotos>> {
            override fun onResponse(
                call: Call<List<ColletionPhotos>>,
                response: Response<List<ColletionPhotos>>
            ) {
                if (response.body() != null) {
                    val key = params.key + 1
                    callback.onResult(response.body()!!, key)
                }
            }

            override fun onFailure(call: Call<List<ColletionPhotos>>, t: Throwable) {
            }
        })
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
