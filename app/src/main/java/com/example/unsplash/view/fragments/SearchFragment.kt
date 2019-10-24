package com.example.unsplash.view.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*


class SearchFragment : Fragment() {
    private lateinit var mAdapter: MyPagedListAdapter
    private val numberOfColumns = 2
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoViewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        (activity as MainActivity).showNavBar()
        (activity as MainActivity).hideFab()
        photoViewModel.searchPagedList.observe(activity as MainActivity, Observer { mAdapter.submitList(it) })

        photoViewModel.photoLikeChangerObject.observe(this, Observer {
            if (it != null) {
                if (it.position != -1) {
                    if (mAdapter.currentList != null) {
                        mAdapter.currentList!![it.position]?.likedByUser = it.isLiked
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
        initView()
    }


    private fun initView() {
        mAdapter = MyPagedListAdapter(requireContext(), photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
        search_rv.adapter = mAdapter
        search_rv.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        search_rv.setEmptyView(empty_tv)
        edit_search.setOnKeyListener { _, _, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                search_button.callOnClick()
            }
            false
        }
        search_button.setOnClickListener {
            fragmentManager?.findFragmentById(R.id.container)?.let { it1 ->
                photoViewModel.photoPagedList
                        .removeObservers(it1)
            }
            photoViewModel.setQuery(edit_search.text.toString())
            photoViewModel.searchPagedList.observe(this, Observer { mAdapter.submitList(it) })
        }
        (activity as AppCompatActivity).setSupportActionBar(search_toolbar)
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
