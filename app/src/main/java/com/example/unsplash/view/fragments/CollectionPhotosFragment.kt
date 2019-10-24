package com.example.unsplash.view.fragments


import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_collection_photos.*
import java.util.*

class CollectionPhotosFragment : Fragment() {

    private lateinit var mAdapter: MyPagedListAdapter
    private var numberOfColumns = 2
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var collectionId: String
    private lateinit var collectionName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoViewModel = ViewModelProvider((Objects.requireNonNull<FragmentActivity>(activity) as MainActivity)).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_collection_photos, container, false)
        (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).hideNavBar()
        assert(arguments != null)
        collectionId = arguments!!.getString("id").toString()
        collectionName = arguments!!.getString("name").toString()
        photoViewModel.setIdCollection(collectionId)
        photoViewModel.collectionPhotosPagedList
                .observe(Objects.requireNonNull<FragmentActivity>(activity), androidx.lifecycle.Observer { mAdapter.submitList(it) })
        viewInit(view)
        return view
    }

    private fun viewInit(view: View) {
        collection_photos_rv.layoutManager = GridLayoutManager(view.context, numberOfColumns)
        mAdapter = MyPagedListAdapter(requireContext(), photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
        collection_photos_rv.adapter = mAdapter
        (Objects.requireNonNull<FragmentActivity>(activity) as AppCompatActivity).setSupportActionBar(collections_photos_toolbar)
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
//    private fun listenerInit() {
//        listener = object : PagedListOnClickListener {
//            override fun onClick(view: View, photo: Photo, position: Int) {
//                val bundle = Bundle()
//                bundle.putString("URI", photo.urls?.regular)
//                bundle.putInt("SMTH", photo.likes!!)
//                bundle.putString("TRANS", view.transitionName)
//                bundle.putString("ID", photo.id)
//                bundle.putBoolean("ISLIKED", photo.likedByUser!!)
//                bundle.putInt("POS", position)
//                reenterTransition = TransitionInflater
//                        .from(context).inflateTransition(android.R.transition.move).setDuration(100)
//                val imageFragment = ImageFragment()
//                imageFragment.arguments = bundle
//                val manager = Objects.requireNonNull<FragmentActivity>(activity)
//                        .supportFragmentManager
//                manager.beginTransaction()
//                        .setReorderingAllowed(true)
//                        // animation works well on emulator, but not on 21api device
//                        .addSharedElement(view, view.transitionName)
//                        .replace(R.id.container, imageFragment)
//                        .addToBackStack(null)
//                        .commit()
//            }
//
//            override fun onClickCollection(view: View, collection: Collection) {
//
//            }
//        }
//    }
}