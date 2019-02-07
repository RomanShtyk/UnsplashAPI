package com.example.unsplash.view;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.unsplash.R;
import com.example.unsplash.model.models.AccessToken;
import com.example.unsplash.model.unsplash.HeaderInterceptor;
import com.example.unsplash.model.unsplash.Unsplash;
import com.example.unsplash.model.unsplash.UnsplashAPI;
import com.example.unsplash.view.fragments.CollectionFragment;
import com.example.unsplash.view.fragments.ListFragment;
import com.example.unsplash.view.fragments.SearchFragment;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    String token = "";
    static boolean isFirst = true;
    final Fragment listFragment = new ListFragment();
    final Fragment searchFragment = new SearchFragment();
    final Fragment collectionFragment = new CollectionFragment();
    Fragment active = listFragment;

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (isFirst) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com/oauth/authorize" + "?client_id=" + Unsplash.CLIENT_ID + "&redirect_uri=" + Unsplash.REDIRECT_URI + "&response_type=code&scope=public+write_likes"));
            startActivity(intent);
            isFirst = false;
        }
        viewInit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null) {
            String code = uri.getQueryParameter("code");

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HeaderInterceptor(Unsplash.CLIENT_ID)).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Unsplash.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UnsplashAPI unsplashAPI = retrofit.create(UnsplashAPI.class);
            unsplashAPI.getAccessToken(Unsplash.CLIENT_ID, Unsplash.SECRET, Unsplash.REDIRECT_URI, code, "authorization_code").enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            token = response.body().getAccess_token();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                    Log.d("mLog", "onFailure: ");
                }
            });
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, collectionFragment, "3").hide(collectionFragment).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, searchFragment, "2").hide(searchFragment).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, listFragment, "1").commit();
    }

    private void viewInit() {
        bottomNav = findViewById(R.id.navigationView);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_collections:
                        getSupportFragmentManager().beginTransaction().hide(active).show(collectionFragment).commit();
                        active = collectionFragment;
                        break;
                    case R.id.navigation_search:
                        getSupportFragmentManager().beginTransaction().hide(active).show(searchFragment).commit();
                        active = searchFragment;
                        break;
                    case R.id.navigation:
                        getSupportFragmentManager().beginTransaction().hide(active).show(listFragment).commit();
                        active = listFragment;
                        break;
                }
                return false;

            }
        });
    }


}
