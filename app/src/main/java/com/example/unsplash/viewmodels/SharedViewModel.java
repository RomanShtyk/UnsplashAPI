package com.example.unsplash.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.unsplash.models.Photo;
import com.example.unsplash.repositories.MainActivityRepo;

import java.util.List;

public class SharedViewModel extends AndroidViewModel {

    private final LiveData<List<Photo>> photoListObservable;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        MainActivityRepo repo = new MainActivityRepo();
        photoListObservable = repo.getPhoto();
    }

    public LiveData<List<Photo>> getPhotos(){
        return photoListObservable;
    }

}