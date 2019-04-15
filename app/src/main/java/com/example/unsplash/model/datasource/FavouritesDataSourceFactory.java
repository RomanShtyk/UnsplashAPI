package com.example.unsplash.model.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.example.unsplash.model.models.Photo;

public class FavouritesDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Photo>> favouritesLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        FavouritesDataSource favouritesDataSource = new FavouritesDataSource();
        favouritesLiveDataSource.postValue(favouritesDataSource);
        return favouritesDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Photo>> getFavouritesLiveDataSource() {
        return favouritesLiveDataSource;
    }
}