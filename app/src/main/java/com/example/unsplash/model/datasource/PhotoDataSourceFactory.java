package com.example.unsplash.model.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.example.unsplash.model.models.Photo;

public class PhotoDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Photo>> photoLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        PhotoDataSource photoDataSource = new PhotoDataSource();
        photoLiveDataSource.postValue(photoDataSource);
        return photoDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Photo>> getPhotoLiveDataSource() {
        return photoLiveDataSource;
    }

}
