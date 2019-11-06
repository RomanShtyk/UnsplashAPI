package com.example.unsplash.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.unsplash.model.datasource.*
import com.example.unsplash.model.models.ColletionPhotos
import com.example.unsplash.model.models.LikePhotoResult
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.model.models.Photo
import com.example.unsplash.model.unsplash.Unsplash
import com.example.unsplash.model.unsplash.UnsplashAPI
import com.example.unsplash.view.MainActivity.Companion.token
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


class PhotoViewModel @Inject constructor() : ViewModel() {
    var list: LiveData<PagedList<Photo>>
    var photoPagedList: LiveData<PagedList<Photo>>
    var favouritesPagedList: LiveData<PagedList<Photo>>
    var searchPagedList: LiveData<PagedList<Photo>>
    var collectionsPagedList: LiveData<PagedList<ColletionPhotos>>
    lateinit var collectionPhotosPagedList: LiveData<PagedList<Photo>>
    var photoLikeChangerObject: MutableLiveData<MyLikeChangerObject>
    private val unsplashAPI =
        Unsplash.getRetrofitPostInstance(token).create<UnsplashAPI>(UnsplashAPI::class.java)

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(10)
        .build()

    init {
        list = LivePagedListBuilder(PhotoDataSourceFactory(), config).build()
        photoPagedList = LivePagedListBuilder(PhotoDataSourceFactory(), config).build()
        searchPagedList = LivePagedListBuilder(SearchDataSourceFactory(""), config).build()
        collectionsPagedList = LivePagedListBuilder(CollectionDataSourceFactory(), config).build()
        favouritesPagedList = LivePagedListBuilder(FavouritesDataSourceFactory(), config).build()

        val my = MyLikeChangerObject("a", false, -1)
        photoLikeChangerObject = MutableLiveData()
        photoLikeChangerObject.value = my
    }

    fun setLike(id: String) {
        unsplashAPI.likeAPhoto(id).enqueue(object : retrofit2.Callback<LikePhotoResult> {
            override fun onResponse(
                call: Call<LikePhotoResult>,
                response: Response<LikePhotoResult>
            ) {
            }

            override fun onFailure(call: Call<LikePhotoResult>, t: Throwable) {
                Log.d("mLog", "onFailure: set Like")
            }
        })
    }

    fun setDislike(id: String) {
        unsplashAPI.dislikeAPhoto(id).enqueue(object : retrofit2.Callback<LikePhotoResult> {
            override fun onResponse(
                call: Call<LikePhotoResult>,
                response: Response<LikePhotoResult>
            ) {
            }

            override fun onFailure(call: Call<LikePhotoResult>, t: Throwable) {
                Log.d("mLog", "onFailure: set Dislike")
            }
        })
    }

    fun changeLike(myLikeChangerObject: MyLikeChangerObject) {
        photoLikeChangerObject.value = myLikeChangerObject
    }

    fun setQuery(query: String) {
        if (query != "") {
            searchPagedList = LivePagedListBuilder(SearchDataSourceFactory(query), config).build()
        }
    }

    fun setIdCollection(id: String) {
        val collectionPhotosDataSourceFactory = CollectionPhotosDataSourceFactory(id)
        collectionPhotosPagedList =
            LivePagedListBuilder(collectionPhotosDataSourceFactory, config).build()
    }

    fun getList(query: String) {
        list.value?.dataSource?.invalidate()
        list = LivePagedListBuilder(
            if (query != "") SearchDataSourceFactory(query) else PhotoDataSourceFactory(),
            config
        ).build()
    }
}
