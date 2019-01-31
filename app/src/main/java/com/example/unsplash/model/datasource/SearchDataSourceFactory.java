package com.example.unsplash.model.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.example.unsplash.model.models.Photo;

public class SearchDataSourceFactory extends DataSource.Factory {

    public SearchDataSourceFactory(String query) {
        this.query = query;
    }

    private MutableLiveData<PageKeyedDataSource<Integer, Photo>> photoLiveDataSource = new MutableLiveData<>();
    private String query;

    @Override
    public DataSource create() {
        SearchDataSource photoDataSource = new SearchDataSource(query);
        photoLiveDataSource.postValue(photoDataSource);
        return photoDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Photo>> getPhotoLiveDataSource() {
        return photoLiveDataSource;
    }
}
