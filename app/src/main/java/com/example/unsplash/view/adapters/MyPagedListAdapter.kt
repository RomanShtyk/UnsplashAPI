package com.example.unsplash.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.unsplash.R
import com.example.unsplash.model.models.Photo
import kotlinx.android.synthetic.main.list_item.view.*

internal class MyPagedListAdapter(
    private val mContext: Context,
    private val photoClickListener: (View, Photo, Int) -> Unit
) : PagedListAdapter<Photo, MyPagedListAdapter.MyPagedViewHolder>(DIFF_CALLBACK) {

    internal inner class MyPagedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(i: Int) {
            itemView.apply {
                val photo = getItem(i)
                picture.transitionName = photo?.urls?.regular
                if (photo != null) {
                    Glide.with(mContext).load(photo.urls?.regular)
                        .apply(
                            RequestOptions().dontTransform() // this line
                        )
                        .thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(picture)
                } else {
                    Log.d("mLog", "onBindViewHolder: photo is null")
                }
                picture.setOnClickListener {
                    if (photo != null) {
                        photoClickListener(picture, photo, i)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyPagedViewHolder {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.list_item, viewGroup, false)
        return MyPagedViewHolder(view)
    }

    override fun onBindViewHolder(myPagedViewHolder: MyPagedViewHolder, i: Int) {
        myPagedViewHolder.bind(i)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(photo: Photo, t1: Photo): Boolean {
                return photo == t1
            }

            override fun areContentsTheSame(photo: Photo, t1: Photo): Boolean {
                return photo.urls?.regular == t1.urls?.regular
            }
        }
    }
}
