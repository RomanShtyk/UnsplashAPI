package com.example.unsplash.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.Collection
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListOneViewAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_collection.*

class CollectionFragment : Fragment() {
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var mAdapter: MyPagedListOneViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoViewModel = ViewModelProvider((activity as MainActivity)).get(PhotoViewModel::class.java)
    }

    private fun refreshList() {
        photoViewModel.collectionPagedList.value?.dataSource?.invalidate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)
        photoViewModel.collectionPagedList.observe(this@CollectionFragment, Observer { mAdapter.submitList(it) })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit()
    }

    private fun viewInit() {
        list_swipe_container.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)

        list_swipe_container.setOnRefreshListener {
            list_swipe_container.isRefreshing = true
            refreshList()
            list_swipe_container.isRefreshing = false
        }

        mAdapter = MyPagedListOneViewAdapter(requireContext(), mListener = { itemView, photo, i -> collectionClick(itemView, photo, i) })
        collection_rv?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectionClick(itemView: View, collection: Collection, i: Int) {
        val bundle = Bundle()
        bundle.putString("id", collection.id.toString())
        bundle.putString("name", collection.title)
        val collectionPhotosFragment = CollectionPhotosFragment()
        collectionPhotosFragment.arguments = bundle
        fragmentManager?.beginTransaction()
                ?.replace(R.id.container, collectionPhotosFragment)
                ?.addToBackStack(null)
                ?.commit()
    }
}
