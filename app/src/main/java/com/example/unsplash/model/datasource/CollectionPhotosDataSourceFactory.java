package com.example.unsplash.model.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.example.unsplash.model.models.Photo;

public class CollectionPhotosDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Photo>> collectionPhotoLiveDataSource = new MutableLiveData<>();
    private String id;

    public CollectionPhotosDataSourceFactory(String id) {
        this.id = id;
    }

    @Override
    public DataSource create() {
        CollectionPhotosDataSource collectionPhotosDataSource = new CollectionPhotosDataSource(id);
        collectionPhotoLiveDataSource.postValue(collectionPhotosDataSource);
        return collectionPhotosDataSource;
    }
}
