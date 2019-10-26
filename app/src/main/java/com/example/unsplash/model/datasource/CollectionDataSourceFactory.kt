package com.example.unsplash.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.unsplash.model.models.ColletionPhotos

class CollectionDataSourceFactory : DataSource.Factory<Int, ColletionPhotos>() {
    private val collectionLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, ColletionPhotos>>()

    override fun create(): DataSource<Int, ColletionPhotos> {
        val collectionDataSource = CollectionDataSource()
        collectionLiveDataSource.postValue(collectionDataSource)
        return collectionDataSource
    }
}
