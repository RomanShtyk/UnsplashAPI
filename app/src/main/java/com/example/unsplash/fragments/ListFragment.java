package com.example.unsplash.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unsplash.R;
import com.example.unsplash.adapters.MyRecyclerViewAdapter;
import com.example.unsplash.models.Photo;
import com.example.unsplash.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    MyRecyclerViewAdapter mAdapter;
    List<Photo> photos = new ArrayList<>();
    SharedViewModel viewModel;
    RecyclerView rv;
    final int numberOfColumns = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mAdapter = new MyRecyclerViewAdapter(view.getContext(), photos);
        rv = view.findViewById(R.id.rView);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        rv.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getActivity() != null;
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getPhotos().observe(getViewLifecycleOwner(), new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photoList) {
                assert photoList != null;
                photos.addAll(photoList);
                mAdapter.updateList(photos);
            }
        });

    }
}
