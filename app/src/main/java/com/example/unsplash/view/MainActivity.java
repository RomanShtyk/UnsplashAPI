package com.example.unsplash.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.unsplash.R;
import com.example.unsplash.view.fragments.ListFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            ListFragment listFragment = new ListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment).commit();
        }
        // TODO: 25.01.2019 почати робити пошук
    }

}
