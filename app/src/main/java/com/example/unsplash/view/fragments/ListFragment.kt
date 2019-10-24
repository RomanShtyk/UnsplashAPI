package com.example.unsplash.view.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*


class ListFragment : Fragment() {
    private lateinit var mAdapter: MyPagedListAdapter
    private val numberOfColumns = 2
    private lateinit var photoViewModel: PhotoViewModel

    private fun refreshList() {
        Objects.requireNonNull<PagedList<Photo>>(photoViewModel.photoPagedList.value).dataSource.invalidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoViewModel = ViewModelProvider(Objects.requireNonNull<FragmentActivity>(activity)).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).showNavBar()
        photoViewModel.photoPagedList.observe(this, Observer { mAdapter.submitList(it) })
        photoViewModel.photoLikeChangerObject.observe(this, Observer {
            if (it != null) {
                if (it.position != -1) {
                    if (mAdapter.currentList != null) {
                        Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likedByUser = it.isLiked

                        //just increment or decrement likes value
                        if (it.isLiked) {
                            Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes = Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes?.plus(1)
                        } else {
                            Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes = Objects.requireNonNull<Photo>(Objects.requireNonNull<PagedList<Photo>>(mAdapter.currentList)[it.position]).likes?.minus(1)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit(view)
    }

    private fun viewInit(view: View) {
        list_rv.layoutManager = GridLayoutManager(view.context, numberOfColumns)
        mAdapter = MyPagedListAdapter(requireContext(), photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
        list_rv.adapter = mAdapter
        (Objects.requireNonNull<FragmentActivity>(activity) as AppCompatActivity).setSupportActionBar(list_toolbar)
        favourite.setOnClickListener {
            val favouritesFragment = FavouritesFragment()
            val manager = Objects.requireNonNull<FragmentActivity>(activity)
                    .supportFragmentManager
            manager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, favouritesFragment)
                    .addToBackStack(null)
                    .commit()
        }
        list_swipe_container.setOnRefreshListener {
            list_swipe_container.isRefreshing = true
            refreshList()
            list_swipe_container.isRefreshing = false
        }

        list_swipe_container.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)

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
        val extras = FragmentNavigatorExtras(
                view to getString(R.string.picture_from_unsplash))
        // (activity as MainActivity).navController.navigate(R.id.action_listFragment_to_imageFragment, bundle, null, extras)
    }

}
