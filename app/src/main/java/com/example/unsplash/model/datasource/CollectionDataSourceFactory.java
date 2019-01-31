package com.example.unsplash.model.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.example.unsplash.model.models.Collection;

public class CollectionDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, Collection>> collectionLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        CollectionDataSource collectionDataSource = new CollectionDataSource();
        collectionLiveDataSource.postValue(collectionDataSource);
        return collectionDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Collection>> getCollectionLiveDataSource() {
        return collectionLiveDataSource;
    }
}
