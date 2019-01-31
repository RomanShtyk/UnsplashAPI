package com.example.unsplash.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unsplash.R;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.view.adapters.MyPagedListOneViewAdapter;
import com.example.unsplash.view.adapters.PagedListOnClickListener;
import com.example.unsplash.viewmodel.PhotoViewModel;

import java.util.Objects;


public class CollectionFragment extends Fragment {
    PhotoViewModel photoViewModel;
    RecyclerView rv;
    MyPagedListOneViewAdapter mAdapter;
    PagedListOnClickListener listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        viewInit(view);
        photoViewModel.collectionPagedList.observe(this, new Observer<PagedList<Collection>>() {
            @Override
            public void onChanged(@Nullable PagedList<Collection> collections) {
                mAdapter.submitList(collections);
                photoViewModel.collectionPagedList.getValue();
            }
        });


        return  view;
    }

    private void viewInit(View view) {
        rv = view.findViewById(R.id.rView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        listenerInit();

        mAdapter = new MyPagedListOneViewAdapter(getActivity(), listener);
        rv.setAdapter(mAdapter);
    }

    private void listenerInit() {
        listener = new PagedListOnClickListener() {
            @Override
            public void onClick(View view, Photo photo) {
                Bundle bundle = new Bundle();
                assert photo != null;
                bundle.putString("URI", photo.getUrls().getRegular());
                bundle.putString("SMTH", photo.getLikes().toString());
                bundle.putString("TRANS", view.getTransitionName());

                setReenterTransition(TransitionInflater
                        .from(getContext()).inflateTransition(android.R.transition.move).setDuration(100));
                ImageFragment imageFragment = new ImageFragment();
                imageFragment.setArguments(bundle);
                FragmentManager manager = (Objects.requireNonNull(getActivity()))
                        .getSupportFragmentManager();
                assert manager != null;
                manager.beginTransaction()
                        //.setReorderingAllowed(true)
                        // animation works well on emulator, but not on 21api device
                        .addSharedElement(view, view.getTransitionName())
                        .replace(R.id.container, imageFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onClickCollection(View view, Collection collection) {
                Bundle bundle = new Bundle();
                assert collection != null;
                bundle.putString("URI", collection.getCoverPhoto().getUrls().getRegular());
                bundle.putString("SMTH", collection.getCoverPhoto().getLikes().toString());
                bundle.putString("TRANS", view.getTransitionName());

                setReenterTransition(TransitionInflater
                        .from(getContext()).inflateTransition(android.R.transition.move).setDuration(100));
                ImageFragment imageFragment = new ImageFragment();
                imageFragment.setArguments(bundle);
                FragmentManager manager = (Objects.requireNonNull(getActivity()))
                        .getSupportFragmentManager();
                assert manager != null;
                manager.beginTransaction()
                        //.setReorderingAllowed(true)
                        // animation works well on emulator, but not on 21api device
                        .addSharedElement(view, view.getTransitionName())
                        .replace(R.id.container, imageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
    }

}
