package com.example.unsplash.view.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.ColletionPhotos
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListOneViewAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_collection.*

class CollectionFragment : Fragment() {
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var mAdapter: MyPagedListOneViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoViewModel =
            ViewModelProvider((activity as MainActivity)).get(PhotoViewModel::class.java)
    }

    private fun refreshList() {
        photoViewModel.colletionPhotosPagedList.value?.dataSource?.invalidate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)
        photoViewModel.colletionPhotosPagedList.observe(
            this@CollectionFragment,
            Observer { mAdapter.submitList(it) })
        return view
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
        exitTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.explode)
                .setDuration(500)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, collectionPhotosFragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}
