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
import com.example.unsplash.model.models.Collection;
import com.squareup.picasso.Picasso;

public class MyPagedListOneViewAdapter extends PagedListAdapter<Collection, MyPagedListOneViewAdapter.MyPagedViewHolderOneView> {

    private Context mContext;
    private PagedListOnClickListener mListener;

    public MyPagedListOneViewAdapter(Context context, PagedListOnClickListener listener) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.mListener = listener;
    }

    class MyPagedViewHolderOneView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        PagedListOnClickListener mpListener;

        MyPagedViewHolderOneView(View view, PagedListOnClickListener listener) {
            super(view);
            image = view.findViewById(R.id.picture);
            mpListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mpListener.onClickCollection(image, getItem(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public MyPagedListOneViewAdapter.MyPagedViewHolderOneView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_picture_main_activity, viewGroup, false);
        return new MyPagedListOneViewAdapter.MyPagedViewHolderOneView(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPagedViewHolderOneView myPagedViewHolderOneView, int i) {
        final Collection collection = getItem(i);
        myPagedViewHolderOneView.image.setTransitionName("" + i);
        if (collection != null) {
            Picasso.get().load(collection.getCoverPhoto().getUrls().getRegular()).noFade().into(myPagedViewHolderOneView.image);
        } else {
            Log.d("mLog", "onBindViewHolder: photo is null");
        }
    }


    private static DiffUtil.ItemCallback<Collection> DIFF_CALLBACK = new DiffUtil.ItemCallback<Collection>() {
        @Override
        public boolean areItemsTheSame(@NonNull Collection collection, @NonNull Collection t1) {
            return collection.equals(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Collection collection, @NonNull Collection t1) {
            return collection.equals(t1);
        }
    };

}
