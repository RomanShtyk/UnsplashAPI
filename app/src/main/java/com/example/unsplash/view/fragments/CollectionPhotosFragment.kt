package com.example.unsplash.view.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_collection_photos.*

class CollectionPhotosFragment : Fragment() {

    private lateinit var mAdapter: MyPagedListAdapter
    private var numberOfColumns = 2
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var collectionId: String
    private lateinit var collectionName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoViewModel =
            ViewModelProvider((activity as MainActivity)).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection_photos, container, false)
        (activity as MainActivity).hideNavBar()
        assert(arguments != null)
        collectionId = arguments!!.getString("id").toString()
        collectionName = arguments!!.getString("name").toString()
        photoViewModel.setIdCollection(collectionId)
        photoViewModel.collectionPhotosPagedList
            .observe(
                activity as MainActivity,
                androidx.lifecycle.Observer { mAdapter.submitList(it) })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit()
    }

    private fun viewInit() {
        collection_photos_rv.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        mAdapter = MyPagedListAdapter(
            requireContext(),
            photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
        collection_photos_rv.adapter = mAdapter
        (activity as MainActivity).setSupportActionBar(collections_photos_toolbar)
        collections_photo_toolbar_tv.text = collectionName
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
        val imageFragment = ImageFragment()
        imageFragment.arguments = bundle
        reenterTransition = TransitionInflater
            .from(context).inflateTransition(android.R.transition.move).setDuration(100)

        activity?.supportFragmentManager?.beginTransaction()
            ?.addSharedElement(view, view.transitionName)
            ?.replace(R.id.container, imageFragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}
