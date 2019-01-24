package com.example.unsplash.view;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.unsplash.R;
import com.example.unsplash.view.fragments.ImageFragment;
import com.example.unsplash.view.fragments.ListFragment;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static ImageFragment imageFragment;
    @SuppressLint("StaticFieldLeak")
    public static ListFragment listFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if(savedInstanceState == null){
            fragmentInit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment).commit();
        }
    }

    private void fragmentInit() {
        imageFragment = new ImageFragment();
        listFragment = new ListFragment();
    }

}
