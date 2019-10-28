package com.example.unsplash.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.example.unsplash.R
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_collection_photos.*

class CollectionPhotosFragment : Fragment() {

    private lateinit var mAdapter: MyPagedListAdapter
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var collectionId: String
    private lateinit var collectionName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoViewModel =
            ViewModelProvider((activity as MainActivity)).get(PhotoViewModel::class.java)
        collectionId = arguments!!.getString("id").toString()
        collectionName = arguments!!.getString("name").toString()
        photoViewModel.setIdCollection(collectionId)
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
        val view = inflater.inflate(R.layout.fragment_collection_photos, container, false)
        postponeEnterTransition()
        addObservers()
        return view
    }

    private fun addObservers() {
        assert(arguments != null)
        photoViewModel.collectionPhotosPagedList
            .observe(
                this,
                Observer { mAdapter.submitList(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun viewInit() {
        (activity as MainActivity).hideNavBar()
        collection_photos_rv.layoutManager = GridLayoutManager(requireContext(), 2)
        mAdapter = MyPagedListAdapter(
            requireContext(),
            photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
        collection_photos_rv.adapter = mAdapter
        (activity as AppCompatActivity).setSupportActionBar(collections_photos_toolbar)
        collections_photo_toolbar_tv.text = collectionName
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
