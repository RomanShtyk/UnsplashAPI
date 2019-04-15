package com.example.unsplash.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    Toolbar toolbar;
    final int numberOfColumns = 2;
    PagedListOnClickListener listener;
    public PhotoViewModel photoViewModel;
    private SwipeRefreshLayout swipeContainer;
    ImageView favouritesButton;

    private void refreshList() {
        Objects.requireNonNull(photoViewModel.photoPagedList.getValue()).getDataSource().invalidate();
    }

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
        photoViewModel.photoPagedList.observe(this, photos -> mAdapter.submitList(photos));
        photoViewModel.photoLikeChangerObject.observe(this, myLikeChangerObject -> {
            assert myLikeChangerObject != null;
            if (!(myLikeChangerObject.getPosition() == -1)) {
                if (mAdapter.getCurrentList() != null) {
                    Objects.requireNonNull(Objects.requireNonNull(mAdapter.getCurrentList()).get(myLikeChangerObject.getPosition())).setLikedByUser(myLikeChangerObject.isLiked());

                    //just increment or decrement likes value
                    if (myLikeChangerObject.isLiked()) {
                        Objects.requireNonNull(Objects.requireNonNull(mAdapter.getCurrentList()).get(myLikeChangerObject.getPosition())).setLikes(Objects.requireNonNull(Objects.requireNonNull(mAdapter.getCurrentList()).get(myLikeChangerObject.getPosition())).getLikes() + 1);
                    } else {
                        Objects.requireNonNull(Objects.requireNonNull(mAdapter.getCurrentList()).get(myLikeChangerObject.getPosition())).setLikes(Objects.requireNonNull(Objects.requireNonNull(mAdapter.getCurrentList()).get(myLikeChangerObject.getPosition())).getLikes() - 1);
                    }

                    mAdapter.notifyItemChanged(myLikeChangerObject.getPosition());
                    MyLikeChangerObject my = new MyLikeChangerObject("a", false, -1);
                    photoViewModel.changeLike(my);
                }
            }
        });

        return view;
    }

    private void viewInit(View view) {
        rv = view.findViewById(R.id.rView);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeContainer.setOnRefreshListener(() -> {
            swipeContainer.setRefreshing(true);
            refreshList();
            swipeContainer.setRefreshing(false);
        });

        // todo "hide nav bar in imagefragment and favouritefragment"
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        listenerInit();
        mAdapter = new MyPagedListAdapter(getActivity(), listener);
        rv.setAdapter(mAdapter);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        favouritesButton = view.findViewById(R.id.favourite);
        favouritesButton.setOnClickListener(v -> {
            FavouritesFragment favouritesFragment = new FavouritesFragment();
            FragmentManager manager = (Objects.requireNonNull(getActivity()))
                    .getSupportFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, favouritesFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void listenerInit() {
        listener = new PagedListOnClickListener() {
            @Override
            public void onClick(View view, Photo photo, int position) {
                Bundle bundle = new Bundle();
                assert photo != null;
                bundle.putString("URI", photo.getUrls().getRegular());
                bundle.putInt("SMTH", photo.getLikes());
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
                        .setReorderingAllowed(true)
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
