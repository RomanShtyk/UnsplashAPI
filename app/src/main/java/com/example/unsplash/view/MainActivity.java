package com.example.unsplash.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.unsplash.R;
import com.example.unsplash.view.fragments.CollectionFragment;
import com.example.unsplash.view.fragments.ListFragment;
import com.example.unsplash.view.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    ListFragment listFragment;
    SearchFragment searchFragment;
    CollectionFragment collectionFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            listFragment = new ListFragment();
            searchFragment = new SearchFragment();
            collectionFragment = new CollectionFragment();
        }
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, searchFragment).hide(searchFragment).commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, collectionFragment).hide(collectionFragment).commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment).addToBackStack(null).commit();
        }
    }



}
