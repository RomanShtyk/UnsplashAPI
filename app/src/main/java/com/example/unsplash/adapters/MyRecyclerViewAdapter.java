package com.example.unsplash.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.unsplash.R;
import com.example.unsplash.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.unsplash.MainActivity.imageFragment;
import static com.example.unsplash.MainActivity.listFragment;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private static List<Photo> photoList;
    private static LayoutInflater lInflater;

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;

    public MyRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        MyRecyclerViewAdapter.photoList = photoList;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(List<Photo> photoList) {
        MyRecyclerViewAdapter.photoList.addAll(photoList);
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
        Picasso.get().load(photoList.get(i).getUrls().getFull())
                .into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fade exitFade = new Fade();
                exitFade.setDuration(FADE_DEFAULT_TIME);
                listFragment.setExitTransition(exitFade);

                // 2. Shared Elements Transition
                TransitionSet enterTransitionSet = new TransitionSet();
                enterTransitionSet.addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move));
                enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
                enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
                imageFragment.setSharedElementEnterTransition(enterTransitionSet);

                // 3. Enter Transition for New Fragment
                Fade enterFade = new Fade();
                enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
                enterFade.setDuration(FADE_DEFAULT_TIME);
                imageFragment.setEnterTransition(enterFade);
                // TODO: 21.01.2019 shared view доробити, погуглити 

                Bundle bundle = new Bundle();
                bundle.putString("URI", photoList.get(i).getUrls().getFull());
                bundle.putString("DESCRIPTION", photoList.get(i).getDescription());
                imageFragment.setArguments(bundle);
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                assert manager != null;
                manager.beginTransaction().addSharedElement(viewHolder.image, viewHolder.image.getTransitionName())
                        .replace(R.id.container, imageFragment).commitAllowingStateLoss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
