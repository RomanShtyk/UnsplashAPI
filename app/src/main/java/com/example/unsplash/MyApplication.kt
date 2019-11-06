package com.example.unsplash

import com.example.unsplash.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

open class MyApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
        return appComponent
    }
}