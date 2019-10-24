package com.example.unsplash.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

import com.example.unsplash.model.models.Photo

class PhotoDataSourceFactory : DataSource.Factory<Int, Photo>() {

    private val photoLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, Photo>>()

    override fun create(): DataSource<Int, Photo> {
        val photoDataSource = PhotoDataSource()
        photoLiveDataSource.postValue(photoDataSource)
        return photoDataSource
    }
}
