package com.example.unsplash.view.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unsplash.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.example.unsplash.view.MainActivity.listFragment;

public class ImageFragment extends Fragment {
    String uri;
    String likes;
    String transName;
    ImageView image;
    TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            setSharedElementEnterTransition(TransitionInflater
                    .from(getContext()).inflateTransition(android.R.transition.move).setDuration(100));
            listFragment.setReenterTransition(TransitionInflater
                    .from(getContext()).inflateTransition(android.R.transition.move).setDuration(100));
        }
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        if (getArguments() != null) {
            uri = getArguments().getString("URI");
            likes = getArguments().getString("SMTH");
            transName = getArguments().getString("TRANS");
        }
        description = view.findViewById(R.id.description);
        description.setText("Likes: " + likes);
        image = view.findViewById(R.id.bigImage);
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
        return view;
    }


}
