package com.example.unsplash;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.example.unsplash.model.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends Application {
    public static List<Photo> photos;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
        photos = new ArrayList<>();
    }

    public static Context getAppContext() {
        return MyApp.context;
    }
}