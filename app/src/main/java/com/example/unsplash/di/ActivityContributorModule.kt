package com.example.unsplash.di

import com.example.unsplash.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityContributorModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity
}
