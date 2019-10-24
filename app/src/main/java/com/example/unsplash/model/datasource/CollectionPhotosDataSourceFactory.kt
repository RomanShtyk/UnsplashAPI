package com.example.unsplash.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.unsplash.model.models.Photo

class CollectionPhotosDataSourceFactory(private val id: String) : DataSource.Factory<Int, Photo>() {

    private val collectionPhotoLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, Photo>>()

    override fun create(): DataSource<Int, Photo> {
        val collectionPhotosDataSource = CollectionPhotosDataSource(id)
        collectionPhotoLiveDataSource.postValue(collectionPhotosDataSource)
        return collectionPhotosDataSource
    }
}
