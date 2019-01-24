package com.example.unsplash.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import com.example.unsplash.model.datasource.PhotoDataSourceFactory;
import com.example.unsplash.model.models.Photo;

public class PhotoViewModel extends ViewModel {
    public LiveData<PagedList<Photo>> photoPagedList;

    public PhotoViewModel(){
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory();
        LiveData<PageKeyedDataSource<Integer, Photo>> liveDataSource =
                photoDataSourceFactory.getPhotoLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();
        photoPagedList = (new LivePagedListBuilder(photoDataSourceFactory, config).build());
    }
}