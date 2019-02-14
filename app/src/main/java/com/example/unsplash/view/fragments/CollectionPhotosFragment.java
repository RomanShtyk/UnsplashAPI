package com.example.unsplash.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.unsplash.R;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.view.MainActivity;
import com.example.unsplash.view.adapters.MyPagedListAdapter;
import com.example.unsplash.view.adapters.PagedListOnClickListener;
import com.example.unsplash.viewmodel.PhotoViewModel;

import java.util.Objects;

public class CollectionPhotosFragment extends Fragment {

    MyPagedListAdapter mAdapter;
    RecyclerView rv;
    final int numberOfColumns = 2;
    PagedListOnClickListener listener;
    PhotoViewModel photoViewModel;
    String collectionId;
    Toolbar toolbar;
    String collectionName;
    TextView collectionNameTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_photos, container, false);

        ((MainActivity) Objects.requireNonNull(getActivity())).hideNavBar();
        assert getArguments() != null;
        photoViewModel.setIdCollection(collectionId);
        photoViewModel.collectionPhotosPagedList.observe(Objects.requireNonNull(getActivity()), new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                mAdapter.submitList(photos);
            }
        });
        collectionId = getArguments().getString("id");
        collectionName = getArguments().getString("name");

        viewInit(view);
        return view;
    }

    private void viewInit(View view) {
        rv = view.findViewById(R.id.rView);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        listenerInit();
        mAdapter = new MyPagedListAdapter(getActivity(), listener);
        rv.setAdapter(mAdapter);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        collectionNameTV = view.findViewById(R.id.toolbar_tv);
        collectionNameTV.setText(collectionName);
    }

    private void listenerInit() {
        listener = new PagedListOnClickListener() {
            @Override
            public void onClick(View view, Photo photo, int position) {
                Bundle bundle = new Bundle();
                assert photo != null;
                bundle.putString("URI", photo.getUrls().getRegular());
                bundle.putString("SMTH", photo.getLikes().toString());
                bundle.putString("TRANS", view.getTransitionName());
                bundle.putString("ID", photo.getId());
                bundle.putBoolean("ISLIKED", photo.getLikedByUser());

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

            }
        };
    }
}