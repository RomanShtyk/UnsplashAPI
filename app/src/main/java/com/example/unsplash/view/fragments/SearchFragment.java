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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.unsplash.R;
import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.view.adapters.MyPagedListAdapter;
import com.example.unsplash.view.adapters.PagedListOnClickListener;
import com.example.unsplash.viewmodel.PhotoViewModel;

import java.util.Objects;


public class SearchFragment extends Fragment {
    RecyclerView rv;
    MyPagedListAdapter mAdapter;
    final int numberOfColumns = 2;
    Toolbar toolbar;
    EditText searchText;
    ImageView searchButton;
    PhotoViewModel photoViewModel;
    PagedListOnClickListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        //noinspection unchecked
        photoViewModel.searchPagedList.observe(Objects.requireNonNull(getActivity()), new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                mAdapter.submitList(photos);
            }
        });
        rv.setAdapter(mAdapter);
        return view;
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

    private void initView(View view) {
        rv = view.findViewById(R.id.rView);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        searchButton = view.findViewById(R.id.search_button);
        searchText = view.findViewById(R.id.editSearch);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    searchButton.callOnClick();
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoViewModel.photoPagedList
                        .removeObservers(Objects.requireNonNull(Objects.requireNonNull(getFragmentManager())
                                .findFragmentById(R.id.container)));
                photoViewModel.setQuery(searchText.getText().toString());
                photoViewModel.searchPagedList.observe(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getFragmentManager())
                        .findFragmentById(R.id.container))), new Observer<PagedList<Photo>>() {
                    @Override
                    public void onChanged(@Nullable PagedList<Photo> photos) {
                        mAdapter.submitList(photos);
                    }
                });
            }
        });
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        listenerInit();
        mAdapter = new MyPagedListAdapter(getActivity(), listener);
    }


}
