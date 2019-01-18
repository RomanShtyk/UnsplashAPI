package com.example.unsplash.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.unsplash.R;
import com.example.unsplash.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.MyViewHolder> {
    private static List<Photo> photoList;
    private static LayoutInflater lInflater;

    public MainActivityRecyclerViewAdapter(Context context, List<Photo> photoList) {
        MainActivityRecyclerViewAdapter.photoList = photoList;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(List<Photo> photoList) {
        MainActivityRecyclerViewAdapter.photoList.clear();
        MainActivityRecyclerViewAdapter.photoList = photoList;
        this.notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
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


    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        Picasso.get().load(photoList.get(i).getUrls().getFull()).resize(1080,1900)
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
