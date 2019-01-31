package com.example.unsplash.view.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.unsplash.R;
import com.example.unsplash.model.models.Photo;
import com.squareup.picasso.Picasso;

public class MyPagedListAdapter extends PagedListAdapter<Photo, MyPagedListAdapter.MyPagedViewHolder> {

    private Context mContext;
    private PagedListOnClickListener mListener;

    public MyPagedListAdapter(Context context, PagedListOnClickListener listener) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.mListener = listener;
    }

    class MyPagedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        PagedListOnClickListener mpListener;

        MyPagedViewHolder(View view, PagedListOnClickListener listener) {
            super(view);
            image = view.findViewById(R.id.picture);
            mpListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mpListener.onClick(image, getItem(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public MyPagedListAdapter.MyPagedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_picture_main_activity, viewGroup, false);
        return new MyPagedViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPagedListAdapter.MyPagedViewHolder myPagedViewHolder, final int i) {
        final Photo photo = getItem(i);
        myPagedViewHolder.image.setTransitionName("" + i);
        if (photo != null) {
            Picasso.get().load(photo.getUrls().getRegular()).noFade().into(myPagedViewHolder.image);
        } else {
            Log.d("mLog", "onBindViewHolder: photo is null");
        }
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
