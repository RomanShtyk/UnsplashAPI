package com.example.unsplash.view.fragments

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.unsplash.R
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.view.MainActivity
import com.example.unsplash.viewmodel.PhotoViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image.*
import java.util.*


class ImageFragment : Fragment() {
    private lateinit var uri: String
    private var likes: Int = 0
    private lateinit var transName: String
    private lateinit var photoId: String
    private var position: Int = 0
    private var isLiked: Boolean = false
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).hideNavBar()
        photoViewModel = ViewModelProvider((Objects.requireNonNull<FragmentActivity>(activity) as MainActivity)).get(PhotoViewModel::class.java)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if (arguments != null) {
            uri = arguments!!.getString("URI").toString()
            likes = arguments!!.getInt("SMTH")
            transName = arguments!!.getString("TRANS").toString()
            photoId = arguments!!.getString("ID").toString()
            isLiked = arguments!!.getBoolean("ISLIKED")
            position = arguments!!.getInt("POS")
        }
        description.text = "Likes: $likes"
        bigImage.transitionName = transName
        Picasso.get().load(uri).noFade().into(bigImage, object : Callback {
            override fun onSuccess() {
                startPostponedEnterTransition()
            }

            override fun onError(e: Exception) {
                startPostponedEnterTransition()
            }
        })
        if (isLiked) {
            checkbox_like.setButtonDrawable(R.drawable.ic_thumb_up_true_24dp)
        } else {
            checkbox_like.setButtonDrawable(R.drawable.ic_thumb_up_grey_24dp)
        }
        checkbox_like.isChecked = isLiked

        checkbox_like.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                photoViewModel.setLike(photoId)
                buttonView.setButtonDrawable(R.drawable.ic_thumb_up_true_24dp)
                val myLikeChangerObject = MyLikeChangerObject(photoId, true, position)
                photoViewModel.changeLike(myLikeChangerObject)
                likes++
                description.text = "Likes:$likes"
            } else {
                photoViewModel.setDislike(photoId)
                buttonView.setButtonDrawable(R.drawable.ic_thumb_up_grey_24dp)
                val myLikeChangerObject = MyLikeChangerObject(photoId, false, position)
                photoViewModel.changeLike(myLikeChangerObject)
                likes--
                description.text = "Likes:$likes"
            }
        }

    }
}