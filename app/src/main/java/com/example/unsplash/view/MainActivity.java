package com.example.unsplash.view;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.unsplash.R;
import com.example.unsplash.view.fragments.CollectionFragment;
import com.example.unsplash.view.fragments.ListFragment;
import com.example.unsplash.view.fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    final Fragment listFragment = new ListFragment();
    final Fragment searchFragment = new SearchFragment();
    final Fragment collectionFragment = new CollectionFragment();
    Fragment active = listFragment;

    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, collectionFragment, "3").hide(collectionFragment).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, searchFragment, "2").hide(searchFragment).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, listFragment, "1").commit();
        viewInit();
    }

    private void viewInit() {
        bottomNav = findViewById(R.id.navigationView);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_collections:
                        getSupportFragmentManager().beginTransaction().hide(active).show(collectionFragment).commit();
                        active = collectionFragment;
                        break;
                    case R.id.navigation_search:
                        getSupportFragmentManager().beginTransaction().hide(active).show(searchFragment).commit();
                        active = searchFragment;
                        break;
                    case  R.id.navigation:
                        getSupportFragmentManager().beginTransaction().hide(active).show(listFragment).commit();
                        active = listFragment;
                        break;
                }
                return false;

            }
        });
    }


}
