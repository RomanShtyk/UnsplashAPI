package com.example.unsplash.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.R
import com.example.unsplash.model.models.Photo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture_main_activity.view.*

internal class MyPagedListAdapter(private val mContext: Context, private val photoClickListener: (View, Photo, Int) -> Unit) : PagedListAdapter<Photo, MyPagedListAdapter.MyPagedViewHolder>(DIFF_CALLBACK) {

    internal inner class MyPagedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(i: Int) {
            itemView.apply {
                val photo = getItem(i)
                picture.transitionName = "" + i
                if (photo != null) {
                    Picasso.get().load(photo.urls?.regular).noFade().into(picture)
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
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_picture_main_activity, viewGroup, false)
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
