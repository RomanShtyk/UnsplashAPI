package com.example.unsplash.view.fragments

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.example.unsplash.R
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.model.models.Photo
import com.example.unsplash.view.MainActivity
import com.example.unsplash.view.adapters.MyPagedListAdapter
import com.example.unsplash.viewmodel.PhotoViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject


class ListFragment : DaggerFragment() {
    private lateinit var mAdapter: MyPagedListAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val photoViewModel: PhotoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]
    }
    private var isSearching = false
    private val observer = Observer<PagedList<Photo>> { mAdapter.submitList(it) }

    private fun refreshList() {
        photoViewModel.list.value?.dataSource?.invalidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
                .setDuration(300)
        enterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
                .setDuration(300)
        addObservers()
        mAdapter = MyPagedListAdapter(
            requireContext(),
            photoClickListener = { itemView, photo, i -> photoClick(itemView, photo, i) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        postponeEnterTransition()
        (activity as MainActivity).showNavBar()
        return view
    }

    private fun addObservers() {
        photoViewModel.list.observe(this, observer)
        photoViewModel.photoLikeChangerObject.observe(this, Observer {
            if (it != null) {
                if (it.position != -1) {
                    if (mAdapter.currentList != null) {
                        mAdapter.currentList!![it.position]?.likedByUser = it.isLiked
                        // just increment or decrement likes value
                        if (it.isLiked) {
                            mAdapter.currentList!![it.position]?.likes =
                                mAdapter.currentList!![it.position]?.likes?.plus(1)
                        } else {
                            mAdapter.currentList!![it.position]?.likes =
                                mAdapter.currentList!![it.position]?.likes?.minus(1)
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
        (activity as MainActivity).showFab()
        list_rv.layoutManager = GridLayoutManager(requireContext(), 2)
        list_rv.adapter = mAdapter
        list_rv.setEmptyView(empty)
        (activity as AppCompatActivity).setSupportActionBar(list_toolbar)
        favourite.setOnClickListener {
            val favouritesFragment = FavouritesFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, favouritesFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        list_swipe_container.setOnRefreshListener {
            if (!isSearching) {
                list_swipe_container.isRefreshing = true
                refreshList()
                list_swipe_container.isRefreshing = false
            }
            list_swipe_container.isRefreshing = false
        }

        list_swipe_container.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        searchView.apply {
            setOnQueryTextListener(object : OnQueryTextListener,
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    isSearching = true
                    query?.let { photoViewModel.setQuery(it) }
                    photoViewModel.list.removeObservers(this@ListFragment)
                    query?.let { photoViewModel.getList(it) }
                    photoViewModel.list.observe(viewLifecycleOwner, observer)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

            setOnCloseListener {
                if (isSearching) {
                    isSearching = false
                    searchView.onActionViewCollapsed()
                    photoViewModel.list.removeObservers(this@ListFragment)
                    photoViewModel.getList("")
                    photoViewModel.list.observe(viewLifecycleOwner, observer)
                    refreshList()
                    true
                } else {
                    searchView.onActionViewCollapsed()
                    true
                }
            }
        }

        morePopup.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.apply {
                menuInflater.inflate(R.menu.more_menu_popup, popup.menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_logout -> {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                            requireContext().startActivity(intent)
                            if (context is Activity) {
                                PreferenceManager.getDefaultSharedPreferences((activity as MainActivity).application)
                                    .edit().clear().apply()
                                (context as Activity).finish()
                            }
                            true
                        }
                        else -> true
                    }
                }
                show()
            }
        }
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
