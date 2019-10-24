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
import com.example.unsplash.model.models.Collection
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_picture_main_activity.view.*

internal class MyPagedListOneViewAdapter(private val mContext: Context, private val mListener: (View, Collection, Int) -> Unit)
    : PagedListAdapter<Collection, MyPagedListOneViewAdapter.MyPagedViewHolderOneView>(DIFF_CALLBACK) {

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
                Picasso.get().load(collection.coverPhoto?.urls?.regular).noFade().into(itemView.picture)
            } else {
                Log.d("mLog", "onBindViewHolder: photo is null")
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyPagedViewHolderOneView {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_picture_main_activity, viewGroup, false)
        return MyPagedViewHolderOneView(view)
    }

    override fun onBindViewHolder(myPagedViewHolderOneView: MyPagedViewHolderOneView, i: Int) {
        myPagedViewHolderOneView.bind(i)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Collection>() {
            override fun areItemsTheSame(collection: Collection, t1: Collection): Boolean {
                return collection == t1
            }

            override fun areContentsTheSame(collection: Collection, t1: Collection): Boolean {
                return collection == t1
            }
        }
    }

}
