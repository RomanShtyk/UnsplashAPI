package com.example.unsplash.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.example.unsplash.R
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_favourites.*
import javax.inject.Inject

class FavouritesFragment : DaggerFragment() {
    private lateinit var mAdapter: MyPagedListAdapter
    private val numberOfColumns = 2
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val photoViewModel: PhotoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]
    }

    private fun refreshList() {
        photoViewModel.favouritesPagedList.value?.dataSource?.invalidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideNavBar()
        exitTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
                .setDuration(100)
        enterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
                .setDuration(100)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        addObservers()
        return view
    }

    private fun addObservers() {
        photoViewModel.favouritesPagedList.observe(
            viewLifecycleOwner,
            Observer { mAdapter.submitList(it) })
        photoViewModel.photoLikeChangerObject.observe(viewLifecycleOwner, Observer {
            assert(it != null)
            if (it != null) {
                if (it.position != -1) {
                    if (mAdapter.currentList != null) {
                        mAdapter.currentList!![it.position]?.likedByUser = it.isLiked
                        // just increment or decrement likes value
                        if (it.isLiked) {
                            mAdapter.currentList!![it.position]?.likes =
                                mAdapter.currentList!![it.position]?.likes!! + 1
                        } else {
                            mAdapter.currentList!![it.position]?.likes =
                                mAdapter.currentList!![it.position]?.likes!! - 1
                        }

                        mAdapter.notifyItemChanged(it.position)
                        val my = MyLikeChangerObject("a", false, -1)
                        photoViewModel.changeLike(my)
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun viewInit() {

        favourites_swipe_container.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        favourites_swipe_container.setOnRefreshListener {
            favourites_swipe_container.isRefreshing = true
            refreshList()
            favourites_swipe_container.isRefreshing = false
        }

        favourites_rv.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        mAdapter = MyPagedListAdapter(
            requireContext(),
            photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
        favourites_rv.adapter = mAdapter
    }

    private fun photoClick(view: View, photo: Photo, position: Int) {
        val bundle = Bundle()
        bundle.apply {
            putString("RAW", photo.urls?.raw)
            putString("URI", photo.urls?.regular)
            photo.likes?.let { putInt("SMTH", it) }
            putString("ID", photo.id)
            photo.likedByUser?.let { putBoolean("ISLIKED", it) }
            putInt("POS", position)
        }

        val imageFragment = ImageFragment()
        imageFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.container, imageFragment)
            ?.addToBackStack("image")
            ?.addSharedElement(view, view.transitionName)
            ?.commit()
    }
}
