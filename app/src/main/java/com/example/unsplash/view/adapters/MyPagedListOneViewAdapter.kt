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
import com.example.unsplash.R
import com.example.unsplash.model.models.ColletionPhotos
import kotlinx.android.synthetic.main.list_item.view.*

internal class MyPagedListOneViewAdapter(
    private val mContext: Context,
    private val mListener: (View, ColletionPhotos, Int) -> Unit
) :
    PagedListAdapter<ColletionPhotos, MyPagedListOneViewAdapter.MyPagedViewHolderOneView>(
        DIFF_CALLBACK
    ) {

    inner class MyPagedViewHolderOneView(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(i: Int) {
            val collection = getItem(i)
            itemView.apply {
                picture.transitionName = "" + i
                setOnClickListener {
                    if (collection != null) {
                        mListener(picture, collection, i)
                    }
                }
            }
            if (collection != null) {
                Glide.with(mContext).load(collection.coverPhoto?.urls?.regular)
                    .thumbnail(0.1f)
                    .into(itemView.picture)
            } else {
                Log.d("mLog", "onBindViewHolder: photo is null")
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyPagedViewHolderOneView {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.list_item, viewGroup, false)
        return MyPagedViewHolderOneView(view)
    }

    override fun onBindViewHolder(myPagedViewHolderOneView: MyPagedViewHolderOneView, i: Int) {
        myPagedViewHolderOneView.bind(i)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ColletionPhotos>() {
            override fun areItemsTheSame(
                colletionPhotos: ColletionPhotos,
                t1: ColletionPhotos
            ): Boolean {
                return colletionPhotos == t1
            }

            override fun areContentsTheSame(
                colletionPhotos: ColletionPhotos,
                t1: ColletionPhotos
            ): Boolean {
                return colletionPhotos == t1
            }
        }
    }
}
