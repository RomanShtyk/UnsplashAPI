package com.example.unsplash.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.unsplash.R;
import com.example.unsplash.view.adapters.MyPagedListAdapter;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.viewmodel.PhotoViewModel;


public class ListFragment extends Fragment {
    MyPagedListAdapter mAdapter;
    RecyclerView rv;
    final int numberOfColumns = 2;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        rv = view.findViewById(R.id.rView);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));

        PhotoViewModel photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        mAdapter = new MyPagedListAdapter(getActivity());
        photoViewModel.photoPagedList.observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                mAdapter.submitList(photos);
            }
        });
        rv.setAdapter(mAdapter);
        return view;
    }

}
