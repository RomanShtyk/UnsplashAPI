package com.example.unsplash.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.unsplash.R;
import com.example.unsplash.model.models.AccessToken;
import com.example.unsplash.model.models.Me;
import com.example.unsplash.model.unsplash.HeaderInterceptor;
import com.example.unsplash.model.unsplash.Unsplash;
import com.example.unsplash.model.unsplash.UnsplashAPI;
import com.example.unsplash.view.fragments.CollectionFragment;
import com.example.unsplash.view.fragments.ListFragment;
import com.example.unsplash.view.fragments.SearchFragment;


import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    public static String token;
    public static String username;
    public final ListFragment listFragment = new ListFragment();
    final SearchFragment searchFragment = new SearchFragment();
    final CollectionFragment collectionFragment = new CollectionFragment();
    Fragment active = listFragment;
    Button login;
    Button loggedIn;
    SharedPreferences sp;
    BottomNavigationView bottomNav;
    private FloatingActionButton fabUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        sp = getApplicationContext().getSharedPreferences("ACCESS_TOKEN",MODE_PRIVATE);
        token = sp.getString("TOKEN", "");
        username = sp.getString("USERNAME", "");
        viewInit();
        if (getIntent().getData() != null) {
            logIn();
        }
    }

    private void logIn() {
        Uri uri = getIntent().getData();
        assert uri != null;
        String code = uri.getQueryParameter("code");
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(Unsplash.CLIENT_ID)).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Unsplash.BASE_URL_POST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UnsplashAPI unsplashAPI = retrofit.create(UnsplashAPI.class);



        Call<AccessToken> call = unsplashAPI.getAccessToken(Unsplash.CLIENT_ID, Unsplash.SECRET, Unsplash.REDIRECT_URI, code, "authorization_code");
        call.enqueue(new Callback<AccessToken>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("TOKEN", Objects.requireNonNull(response.body()).getAccess_token());
                    token = Objects.requireNonNull(response.body()).getAccess_token();
                    editor.apply();
                    UnsplashAPI unsplashAPITokened = Unsplash.getRetrofitPostInstance(token).create(UnsplashAPI.class);
                    Call<Me> callMe = unsplashAPITokened.getMeProfile();
                    callMe.enqueue(new Callback<Me>() {
                        @Override
                        public void onResponse(@NonNull Call<Me> call, @NonNull Response<Me> response) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USERNAME", Objects.requireNonNull(response.body()).username);
                            editor.apply();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Me> call, @NonNull Throwable t) {
                            Log.d("mLog", "ME onFailure: ");
                        }
                    });
                    login.setVisibility(View.GONE);
                    loggedIn.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, collectionFragment, "3").hide(collectionFragment).commit();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, searchFragment, "2").hide(searchFragment).commit();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, listFragment, "1").commit();
                    bottomNav.setVisibility(View.VISIBLE);
                    fabUpload.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                Log.d("mLog", "onFailure: ");
            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void viewInit() {
        login = findViewById(R.id.login);

        loggedIn = findViewById(R.id.logged_in);
        if (token.equals("")) {
            loggedIn.setVisibility(View.GONE);
        }
        login.setOnClickListener(v -> {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com/oauth/authorize" + "?client_id=" + Unsplash.CLIENT_ID + "&redirect_uri=" + Unsplash.REDIRECT_URI + "&response_type=code&scope=public+write_likes"));
        startActivity(intent);
        });
        loggedIn.setOnClickListener(v -> {
            if(token.equals("")){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You should be logged in to continue!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                loggedIn.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, collectionFragment, "3").hide(collectionFragment).commit();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, searchFragment, "2").hide(searchFragment).commit();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, listFragment, "1").commit();
                bottomNav.setVisibility(View.VISIBLE);
                fabUpload.setVisibility(View.VISIBLE);
            }
        });
        fabUpload = findViewById(R.id.fab);
        fabUpload.setOnClickListener(view -> {
            try {
                Uri uri = Uri.parse(Unsplash.UNSPLASH_UPLOAD_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        bottomNav = findViewById(R.id.navigationView);
        bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
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

        });
    }

    @SuppressLint("RestrictedApi")
    public void hideNavBar() {
        bottomNav.setVisibility(View.GONE);
        fabUpload.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    public void showNavBar() {
        bottomNav.setVisibility(View.VISIBLE);
        fabUpload.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    public void hideFab() {
        fabUpload.setVisibility(View.GONE);
    }

}
