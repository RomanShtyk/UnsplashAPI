package com.example.unsplash;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.unsplash.adapters.MainActivityRecyclerViewAdapter;
import com.example.unsplash.models.Photo;
import com.example.unsplash.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    final int numberOfColumns = 2;
    final int jop = 0;
    MainActivityRecyclerViewAdapter mAdapter;
    List<Photo> photos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mAdapter = new MainActivityRecyclerViewAdapter(this, photos);
        MainActivityViewModel viewModel = ViewModelProviders
                .of(this).get(MainActivityViewModel.class);
        observeViewModel(viewModel);
        initRecycler();
    }

    private void observeViewModel(MainActivityViewModel viewModel) {
        viewModel.getPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                if(photos != null){
                    mAdapter.updateList(photos);
                }
            }
        });
    }

    private void initRecycler(){
        rv = findViewById(R.id.rView);
        rv.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        rv.setAdapter(mAdapter);
    }
}
