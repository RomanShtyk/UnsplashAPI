package com.example.unsplash.view.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_favourites.*
import java.util.*


class FavouritesFragment : Fragment() {
    private lateinit var mAdapter: MyPagedListAdapter
    private val numberOfColumns = 2
    private lateinit var photoViewModel: PhotoViewModel

    private fun refreshList() {
        Objects.requireNonNull<PagedList<Photo>>(photoViewModel.favouritesPagedList.value).dataSource.invalidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).hideNavBar()
        photoViewModel = ViewModelProvider((Objects.requireNonNull<FragmentActivity>(activity) as MainActivity)).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        viewInit(view)
        photoViewModel.favouritesPagedList.observe(this, Observer { mAdapter.submitList(it) })
        photoViewModel.photoLikeChangerObject.observe(this, Observer {
            assert(it != null)
            if (it != null) {
                if (it.position != -1) {
                    if (mAdapter.currentList != null) {
                        Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likedByUser = it.isLiked
                        //just increment or decrement likes value
                        if (it.isLiked) {
                            Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes = Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes!! + 1
                        } else {
                            Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes = Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes!! - 1
                        }

                        mAdapter.notifyItemChanged(it.position)
                        val my = MyLikeChangerObject("a", false, -1)
                        photoViewModel.changeLike(my)
                    }
                }
            }
        })

        return view
    }

    private fun viewInit(view: View) {

        favourites_swipe_container.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)


        favourites_swipe_container.setOnRefreshListener {
            favourites_swipe_container.isRefreshing = true
            refreshList()
            favourites_swipe_container.isRefreshing = false
        }


        favourites_rv.layoutManager = GridLayoutManager(view.context, numberOfColumns)
        mAdapter = MyPagedListAdapter(requireContext(), photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
        favourites_rv.adapter = mAdapter
    }

    private fun photoClick(view: View, photo: Photo, position: Int) {
        val bundle = Bundle()
        bundle.apply {
            putString("URI", photo.urls?.regular)
            putInt("SMTH", photo.likes!!)
            putString("TRANS", view.transitionName)
            putString("ID", photo.id)
            putBoolean("ISLIKED", photo.likedByUser!!)
            putInt("POS", position)
        }
        reenterTransition = TransitionInflater
                .from(context).inflateTransition(android.R.transition.move).setDuration(100)
        val imageFragment = ImageFragment()
        imageFragment.arguments = bundle
        val manager = Objects.requireNonNull<FragmentActivity>(activity)
                .supportFragmentManager
        manager.beginTransaction()
                .setReorderingAllowed(true)
                // animation works well on emulator, but not on 21api device
                .addSharedElement(view, view.transitionName)
                .replace(R.id.container, imageFragment)
                .addToBackStack(null)
                .commit()
    }


}