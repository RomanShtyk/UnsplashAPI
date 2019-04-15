package com.example.unsplash.view.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unsplash.R;
import com.example.unsplash.model.models.MyLikeChangerObject;
import com.example.unsplash.view.MainActivity;
import com.example.unsplash.viewmodel.PhotoViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ImageFragment extends Fragment {
    String uri;
    int likes;
    String transName;
    String photoId;
    int position;
    boolean isLiked;
    ImageView image;
    TextView description;
    CheckBox likeButton;
    PhotoViewModel photoViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).hideNavBar();
        photoViewModel = ViewModelProviders.of(getActivity()).get(PhotoViewModel.class);
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
            likes = getArguments().getInt("SMTH");
            transName = getArguments().getString("TRANS");
            photoId = getArguments().getString("ID");
            isLiked = getArguments().getBoolean("ISLIKED");
            position = getArguments().getInt("POS");
        }

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

        likeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                photoViewModel.setLike(photoId);
                buttonView.setButtonDrawable(R.drawable.ic_thumb_up_true_24dp);
                MyLikeChangerObject myLikeChangerObject = new MyLikeChangerObject(photoId, true, position);
                photoViewModel.changeLike(myLikeChangerObject);
                likes++;
                description.setText("Likes:" + likes);
            } else {
                photoViewModel.setDislike(photoId);
                buttonView.setButtonDrawable(R.drawable.ic_thumb_up_grey_24dp);
                MyLikeChangerObject myLikeChangerObject = new MyLikeChangerObject(photoId, false, position);
                photoViewModel.changeLike(myLikeChangerObject);
                likes--;
                description.setText("Likes:" + likes);
            }
        });

    }
}