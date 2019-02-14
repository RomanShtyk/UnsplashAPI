package com.example.unsplash.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unsplash.R;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.view.MainActivity;
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
        ((MainActivity) Objects.requireNonNull(getActivity())).showNavBar();
        viewInit(view);
        photoViewModel.collectionPagedList.observe(this, new Observer<PagedList<Collection>>() {
            @Override
            public void onChanged(@Nullable PagedList<Collection> collections) {
                mAdapter.submitList(collections);
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
            public void onClick(View view, Photo photo, int position) {
            }
            @Override
            public void onClickCollection(View view, Collection collection) {
                Bundle bundle = new Bundle();
                assert collection != null;
                bundle.putString("id", collection.getId().toString());
                bundle.putString("name", collection.getTitle());
                CollectionPhotosFragment collectionPhotosFragment = new CollectionPhotosFragment();
                collectionPhotosFragment.setArguments(bundle);
                Objects.requireNonNull(getFragmentManager()).beginTransaction()
                        .replace(R.id.container, collectionPhotosFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
    }

}
