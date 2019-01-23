package com.example.unsplash.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.unsplash.R;
import com.example.unsplash.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.unsplash.MainActivity.imageFragment;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<Photo> photoList;
    private static LayoutInflater lInflater;


    public MyRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<Photo> photos) {
        this.photoList = photos;
        this.notifyDataSetChanged();
    }

    public void updateList(List<Photo> photos) {
        this.photoList.addAll(photos);
        this.notifyDataSetChanged();
    }

    static class MyViewHolder extends ViewHolder {
        ImageView image;

        MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.picture);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = lInflater.inflate(R.layout.item_picture_main_activity, viewGroup, false);
        return new MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        Picasso.get()
                .load(photoList.get(i).getUrls().getRegular())
                .into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("URI", photoList.get(i).getUrls().getRegular());
                bundle.putString("DESCRIPTION", photoList.get(i).getDescription());

                imageFragment.setArguments(bundle);
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                assert manager != null;
                manager.beginTransaction().setReorderingAllowed(true).addSharedElement(viewHolder.image, viewHolder.image.getTransitionName())
                        .replace(R.id.container, imageFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
