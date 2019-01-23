package com.example.unsplash.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.unsplash.models.Photo;
import com.example.unsplash.repositories.MainActivityRepo;

import java.util.List;
import java.util.Objects;

public class SharedViewModel extends AndroidViewModel {
    private MainActivityRepo repo;
    private LiveData<List<Photo>> photoListObservable;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        repo = new MainActivityRepo();
        photoListObservable = repo.getPhoto();
    }
    public LiveData<List<Photo>> getNewPhotos(){
        MainActivityRepo repo = new MainActivityRepo();
        photoListObservable = repo.getPhoto();
        List<Photo> pho = photoListObservable.getValue();
        assert pho != null;
        pho.addAll(Objects.requireNonNull(repo.getPhoto().getValue()));
        return photoListObservable;
    }
    public LiveData<List<Photo>> getPhotos(){
        return photoListObservable;
    }

}