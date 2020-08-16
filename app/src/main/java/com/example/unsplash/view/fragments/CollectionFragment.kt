package com.example.unsplash.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.ColletionPhotos
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListOneViewAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_collection.*
import javax.inject.Inject

class CollectionFragment : DaggerFragment() {
    private lateinit var mAdapter: MyPagedListOneViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition =
            androidx.transition.TransitionInflater.from(context)
                .inflateTransition(android.R.transition.fade)
        enterTransition =
            androidx.transition.TransitionInflater.from(context)
                .inflateTransition(android.R.transition.fade)
        photoViewModel = provideViewModel()
    }

    private fun provideViewModel() =
        ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java].apply {
            collectionsPagedList.observe(viewLifecycleOwner, Observer {
                mAdapter.submitList(it)
            })
        }

    private fun refreshList() {
        photoViewModel.collectionsPagedList.value?.dataSource?.invalidate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showNavBar()
        (activity as MainActivity).hideFab()
        viewInit()
    }

    private fun viewInit() {
        list_swipe_container.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        list_swipe_container.setOnRefreshListener {
            list_swipe_container.isRefreshing = true
            refreshList()
            list_swipe_container.isRefreshing = false
        }

        mAdapter = MyPagedListOneViewAdapter(
            requireContext(),
            mListener = { itemView, photo, i -> collectionClick(itemView, photo, i) })
        collection_rv?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectionClick(itemView: View, colletionPhotos: ColletionPhotos, i: Int) {
        val bundle = Bundle()
        bundle.putString("id", colletionPhotos.id.toString())
        bundle.putString("name", colletionPhotos.title)
        val collectionPhotosFragment = CollectionPhotosFragment()
        collectionPhotosFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, collectionPhotosFragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}
