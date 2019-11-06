package com.example.unsplash.di

import com.example.unsplash.view.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentContributorModule {

    @ContributesAndroidInjector
    internal abstract fun contributeListFragment(): ListFragment

    @ContributesAndroidInjector
    internal abstract fun contributeImageFragment(): ImageFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCollectionFragment(): CollectionFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCollectionPhotosFragment(): CollectionPhotosFragment

    @ContributesAndroidInjector
    internal abstract fun contributeFavouritesFragment(): FavouritesFragment

    @ContributesAndroidInjector
    internal abstract fun contributeStartPointFragment(): StartPointFragment

}
