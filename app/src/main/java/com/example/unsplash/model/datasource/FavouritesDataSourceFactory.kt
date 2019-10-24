package com.example.unsplash.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

import com.example.unsplash.model.models.Photo

class FavouritesDataSourceFactory : DataSource.Factory<Int, Photo>() {

    private val favouritesLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, Photo>>()

    override fun create(): DataSource<Int, Photo> {
        val favouritesDataSource = FavouritesDataSource()
        favouritesLiveDataSource.postValue(favouritesDataSource)
        return favouritesDataSource
    }
}