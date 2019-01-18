package com.example.unsplash;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApp.context;
    }
}