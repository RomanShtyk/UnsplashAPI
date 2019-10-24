package com.example.unsplash.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.unsplash.model.models.Collection

class CollectionDataSourceFactory : DataSource.Factory<Int, Collection>() {
    private val collectionLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, Collection>>()

    override fun create(): DataSource<Int, Collection> {
        val collectionDataSource = CollectionDataSource()
        collectionLiveDataSource.postValue(collectionDataSource)
        return collectionDataSource
    }
}
