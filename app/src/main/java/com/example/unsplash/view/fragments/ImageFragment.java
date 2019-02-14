package com.example.unsplash.view.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unsplash.R;
import com.example.unsplash.model.models.LikePhotoResult;
import com.example.unsplash.model.unsplash.Unsplash;
import com.example.unsplash.model.unsplash.UnsplashAPI;
import com.example.unsplash.view.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.unsplash.view.MainActivity.token;


public class ImageFragment extends Fragment {
    String uri;
    String likes;
    String transName;
    String photoId;
    boolean isLiked;
    ImageView image;
    TextView description;
    CheckBox likeButton;
    public UnsplashAPI unsplashAPI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).hideNavBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            setSharedElementEnterTransition(TransitionInflater
                    .from(getContext()).inflateTransition(android.R.transition.move).setDuration(100));
        }
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        initView(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        if (getArguments() != null) {
            uri = getArguments().getString("URI");
            likes = getArguments().getString("SMTH");
            transName = getArguments().getString("TRANS");
            photoId = getArguments().getString("ID");
            isLiked = getArguments().getBoolean("ISLIKED");
        }
        unsplashAPI = Unsplash.getRetrofitPostInstance(token).create(UnsplashAPI.class);

        description = view.findViewById(R.id.description);
        likeButton = view.findViewById(R.id.checkbox_like);
        image = view.findViewById(R.id.bigImage);

        description.setText("Likes: " + likes);
        image.setTransitionName(transName);
        Picasso.get().load(uri).noFade().into(image, new Callback() {
            @Override
            public void onSuccess() {
                startPostponedEnterTransition();
            }

            @Override
            public void onError(Exception e) {
                startPostponedEnterTransition();
            }
        });
        if (isLiked) {
            likeButton.setButtonDrawable(R.drawable.ic_thumb_up_true_24dp);
        } else {
            likeButton.setButtonDrawable(R.drawable.ic_thumb_up_grey_24dp);
        }
        likeButton.setChecked(isLiked);

        likeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    unsplashAPI.likeAPhoto(photoId).enqueue(new retrofit2.Callback<LikePhotoResult>() {
                        @Override
                        public void onResponse(@NonNull Call<LikePhotoResult> call, @NonNull Response<LikePhotoResult> response) {
                            if (response.isSuccessful()) {
                                buttonView.setButtonDrawable(R.drawable.ic_thumb_up_true_24dp);
                                //crutch for updating pagedlist
                                ((MainActivity) Objects.requireNonNull(getActivity())).listFragment.refreshList();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<LikePhotoResult> call, @NonNull Throwable t) {

                        }
                    });
                } else {
                    unsplashAPI.dislikeAPhoto(photoId).enqueue(new retrofit2.Callback<LikePhotoResult>() {
                        @Override
                        public void onResponse(@NonNull Call<LikePhotoResult> call, @NonNull Response<LikePhotoResult> response) {
                            buttonView.setButtonDrawable(R.drawable.ic_thumb_up_grey_24dp);
                            //crutch for updating pagedlist
                            ((MainActivity) Objects.requireNonNull(getActivity())).listFragment.refreshList();
                        }

                        @Override
                        public void onFailure(@NonNull Call<LikePhotoResult> call, @NonNull Throwable t) {

                        }
                    });

                }
            }
        });

    }
}
