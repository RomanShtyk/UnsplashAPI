package com.example.unsplash.view.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.unsplash.R;
import com.example.unsplash.model.models.Photo;
import com.squareup.picasso.Picasso;

import static com.example.unsplash.view.MainActivity.imageFragment;
import static com.example.unsplash.view.MainActivity.listFragment;

public class MyPagedListAdapter extends PagedListAdapter<Photo, MyPagedListAdapter.MyPagedViewHolder> {

    private Context mContext;

    public MyPagedListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.mContext = context;
    }


    class MyPagedViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        MyPagedViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.picture);
        }
    }
    @NonNull
    @Override
    public MyPagedListAdapter.MyPagedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_picture_main_activity, viewGroup, false);
        return new MyPagedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPagedListAdapter.MyPagedViewHolder myPagedViewHolder, final int i){
        final Photo photo = getItem(i);
        myPagedViewHolder.image.setTransitionName("" + i);
        if(photo != null){
            Picasso.get().load(photo.getUrls().getRegular()).noFade().into(myPagedViewHolder.image);
        }else {
            Log.d("mLog", "onBindViewHolder: photo is null");
        }
        myPagedViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                assert photo != null;
                bundle.putString("URI", photo.getUrls().getRegular());
                bundle.putString("SMTH", photo.getLikes().toString());
                bundle.putString("TRANS", myPagedViewHolder.image.getTransitionName());

                listFragment.setSharedElementReturnTransition(TransitionInflater
                        .from(mContext).inflateTransition(android.R.transition.move));

                imageFragment.setArguments(bundle);
                FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                assert manager != null;
                manager.beginTransaction().setReorderingAllowed(true)
                        .addSharedElement(myPagedViewHolder.image, myPagedViewHolder.image.getTransitionName())
                        .replace(R.id.container, imageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private static DiffUtil.ItemCallback<Photo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo photo, @NonNull Photo t1) {
            return photo.equals(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo photo, @NonNull Photo t1) {
            return photo.getUrls().getRegular().equals(t1.getUrls().getRegular());
        }
    };
}
