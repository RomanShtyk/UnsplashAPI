package com.example.unsplash.fragments;

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

public class ImageFragment extends Fragment {
    String uri;
    String descriptionText;
    ImageView image;
    TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            setSharedElementEnterTransition(TransitionInflater
                    .from(getContext()).inflateTransition(android.R.transition.move).setDuration(1000));
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        // TODO: 23.01.2019 розібратися з transition name
        if (getArguments() != null){
            uri = getArguments().getString("URI");
            descriptionText = getArguments().getString("DESCRIPTION");
        }
        description = view.findViewById(R.id.description);
        description.setText(descriptionText);
        image = view.findViewById(R.id.bigImage);
        Picasso.get().load(uri).into(image, new Callback() {
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
