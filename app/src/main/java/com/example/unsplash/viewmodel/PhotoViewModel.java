package com.example.unsplash.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.unsplash.model.datasource.CollectionDataSourceFactory;
import com.example.unsplash.model.datasource.CollectionPhotosDataSourceFactory;
import com.example.unsplash.model.datasource.PhotoDataSourceFactory;
import com.example.unsplash.model.datasource.SearchDataSourceFactory;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.LikePhotoResult;
import com.example.unsplash.model.models.MyLikeChangerObject;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.model.unsplash.Unsplash;
import com.example.unsplash.model.unsplash.UnsplashAPI;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.unsplash.view.MainActivity.token;


public class PhotoViewModel extends ViewModel {
    public LiveData<PagedList<Photo>> photoPagedList;
    public LiveData<PagedList<Photo>> searchPagedList;
    public LiveData<PagedList<Collection>> collectionPagedList;
    public LiveData<PagedList<Photo>> collectionPhotosPagedList;
    public MutableLiveData<MyLikeChangerObject> photoLikeChangerObject;
    private UnsplashAPI unsplashAPI = Unsplash.getRetrofitPostInstance(token).create(UnsplashAPI.class);

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

    public void setLike(String id) {
        unsplashAPI.likeAPhoto(id).enqueue(new retrofit2.Callback<LikePhotoResult>() {
            @Override
            public void onResponse(@NonNull Call<LikePhotoResult> call, @NonNull Response<LikePhotoResult> response) {
            }

            @Override
            public void onFailure(@NonNull Call<LikePhotoResult> call, @NonNull Throwable t) {
                Log.d("mLog", "onFailure: set Like");
            }
        });
    }

    public void setDislike(String id) {
        unsplashAPI.dislikeAPhoto(id).enqueue(new retrofit2.Callback<LikePhotoResult>() {
            @Override
            public void onResponse(@NonNull Call<LikePhotoResult> call, @NonNull Response<LikePhotoResult> response) {
            }

            @Override
            public void onFailure(@NonNull Call<LikePhotoResult> call, @NonNull Throwable t) {
                Log.d("mLog", "onFailure: set Dislike");
            }
        });
    }

    public void changeLike(MyLikeChangerObject myLikeChangerObject) {
        photoLikeChangerObject.setValue(myLikeChangerObject);
    }

    public void setQuery(String query) {
        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(query);
        //noinspection deprecation,unchecked
        searchPagedList = new LivePagedListBuilder(searchDataSourceFactory, config).build();
    }

    public void setIdCollection(String id) {
        CollectionPhotosDataSourceFactory collectionPhotosDataSourceFactory = new CollectionPhotosDataSourceFactory(id);
        //noinspection deprecation,unchecked
        collectionPhotosPagedList = new LivePagedListBuilder(collectionPhotosDataSourceFactory, config).build();
    }
}
