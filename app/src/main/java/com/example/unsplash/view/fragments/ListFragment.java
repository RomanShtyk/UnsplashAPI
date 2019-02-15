package com.example.unsplash.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unsplash.R;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.MyLikeChangerObject;
import com.example.unsplash.view.MainActivity;
import com.example.unsplash.view.adapters.MyPagedListAdapter;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.view.adapters.PagedListOnClickListener;
import com.example.unsplash.viewmodel.PhotoViewModel;

import java.util.Objects;


public class ListFragment extends Fragment {
    MyPagedListAdapter mAdapter;
    RecyclerView rv;
    final int numberOfColumns = 2;
    PagedListOnClickListener listener;
    public PhotoViewModel photoViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PhotoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ((MainActivity) Objects.requireNonNull(getActivity())).showNavBar();
        viewInit(view);
        photoViewModel.photoPagedList.observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                mAdapter.submitList(photos);
            }
        });
        photoViewModel.photoLikeChangerObject.observe(this, new Observer<MyLikeChangerObject>() {
            @Override
            public void onChanged(@Nullable MyLikeChangerObject myLikeChangerObject) {
                assert myLikeChangerObject != null;
                if(!(myLikeChangerObject.getPosition() == -1)){
                    if(mAdapter.getCurrentList() != null) {
                        Objects.requireNonNull(Objects.requireNonNull(mAdapter.getCurrentList()).get(myLikeChangerObject.getPosition())).setLikedByUser(myLikeChangerObject.isLiked());
                        mAdapter.notifyItemChanged(myLikeChangerObject.getPosition());
                        MyLikeChangerObject my = new MyLikeChangerObject("a", false, -1);
                        photoViewModel.changeLike(my);
                    }
                }
            }
        });

        return view;
    }

    private void viewInit(View view) {
        rv = view.findViewById(R.id.rView);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        listenerInit();
        mAdapter = new MyPagedListAdapter(getActivity(), listener);
        rv.setAdapter(mAdapter);
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
                bundle.putInt("POS", position);
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
