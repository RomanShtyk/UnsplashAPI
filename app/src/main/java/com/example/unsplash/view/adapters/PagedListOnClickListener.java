package com.example.unsplash.view.adapters;

import android.view.View;

import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.Photo;

public interface PagedListOnClickListener {
    void onClick(View view, Photo photo);
    void onClickCollection(View view, Collection collection);
}
