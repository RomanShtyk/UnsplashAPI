package com.example.unsplash.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.unsplash.model.datasource.CollectionDataSourceFactory;
import com.example.unsplash.model.datasource.PhotoDataSourceFactory;
import com.example.unsplash.model.datasource.SearchDataSourceFactory;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.Photo;


public class PhotoViewModel extends ViewModel {
    public LiveData<PagedList<Photo>> photoPagedList;
    public LiveData<PagedList<Photo>> searchPagedList;
    public LiveData<PagedList<Collection>> collectionPagedList;

    public PhotoViewModel() {
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory();
        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .build();
        //noinspection deprecation,unchecked
        photoPagedList = new LivePagedListBuilder(photoDataSourceFactory, config).build();

        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory("random");
        //noinspection deprecation,unchecked
        searchPagedList = new LivePagedListBuilder(searchDataSourceFactory, config).build();
        CollectionDataSourceFactory collectionDataSourceFactory = new CollectionDataSourceFactory();
        //noinspection deprecation,unchecked
        collectionPagedList = new LivePagedListBuilder(collectionDataSourceFactory, config).build();
    }

    public void setQuery(String query) {
        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(query);

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .build();
        //noinspection deprecation,unchecked
        searchPagedList = new LivePagedListBuilder(searchDataSourceFactory, config).build();
    }

}
