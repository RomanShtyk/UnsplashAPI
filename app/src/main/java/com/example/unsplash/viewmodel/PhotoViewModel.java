package com.example.unsplash.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.unsplash.model.datasource.CollectionDataSourceFactory;
import com.example.unsplash.model.datasource.CollectionPhotosDataSourceFactory;
import com.example.unsplash.model.datasource.PhotoDataSourceFactory;
import com.example.unsplash.model.datasource.SearchDataSourceFactory;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.MyLikeChangerObject;
import com.example.unsplash.model.models.Photo;


public class PhotoViewModel extends ViewModel {
    public LiveData<PagedList<Photo>> photoPagedList;
    public LiveData<PagedList<Photo>> searchPagedList;
    public LiveData<PagedList<Collection>> collectionPagedList;
    public LiveData<PagedList<Photo>> collectionPhotosPagedList;
    public MutableLiveData<MyLikeChangerObject> photoLikeChangerObject;

    private PagedList.Config config =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(false)
                    .setPageSize(10)
                    .build();

    public PhotoViewModel() {
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory();
        //noinspection deprecation,unchecked
        photoPagedList = new LivePagedListBuilder(photoDataSourceFactory, config).build();

        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory("random");
        //noinspection deprecation,unchecked
        searchPagedList = new LivePagedListBuilder(searchDataSourceFactory, config).build();

        CollectionDataSourceFactory collectionDataSourceFactory = new CollectionDataSourceFactory();
        //noinspection deprecation,unchecked
        collectionPagedList = new LivePagedListBuilder(collectionDataSourceFactory, config).build();

        MyLikeChangerObject my = new MyLikeChangerObject("a", false, -1);
        photoLikeChangerObject = new MutableLiveData<>();
        photoLikeChangerObject.setValue(my);
    }

    public void changeLike(MyLikeChangerObject myLikeChangerObject){
        photoLikeChangerObject.setValue(myLikeChangerObject);
    }

    public void setQuery(String query) {
        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(query);
        //noinspection deprecation,unchecked
        searchPagedList = new LivePagedListBuilder(searchDataSourceFactory, config).build();
    }

    public void setIdCollection(String id){
            CollectionPhotosDataSourceFactory collectionPhotosDataSourceFactory = new CollectionPhotosDataSourceFactory(id);
            //noinspection deprecation,unchecked
            collectionPhotosPagedList = new LivePagedListBuilder(collectionPhotosDataSourceFactory, config).build();
    }
}
