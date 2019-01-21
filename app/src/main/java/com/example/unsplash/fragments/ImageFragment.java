package com.example.unsplash.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unsplash.R;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {
    String uri;
    String descriptionText;
    ImageView image;
    TextView description;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        if (getArguments() != null){
           // uri = getArguments().getString("URI");
            descriptionText = getArguments().getString("DESCRIPTION");
        }
        description = view.findViewById(R.id.description);
        description.setText(descriptionText);
        //image = view.findViewById(R.id.bigImage);
        //Picasso.get().load(uri).into(image);
        return view;
    }
}
