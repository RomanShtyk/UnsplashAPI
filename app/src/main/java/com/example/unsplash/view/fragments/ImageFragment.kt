package com.example.unsplash.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.unsplash.R
import com.example.unsplash.model.models.MyLikeChangerObject
import com.example.unsplash.view.MainActivity
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_image.*

class ImageFragment : Fragment() {
    private lateinit var uri: String
    private var likes: Int = 0
    private lateinit var photoId: String
    private var position: Int = 0
    private var isLiked: Boolean = false
    private lateinit var photoViewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideNavBar()
        photoViewModel =
            ViewModelProvider((activity as MainActivity)).get(PhotoViewModel::class.java)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.default_transition)
        exitTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
        enterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            photoId = arguments!!.getString("ID").toString()
            isLiked = arguments!!.getBoolean("ISLIKED")
            position = arguments!!.getInt("POS")
        }
        bigImage.transitionName = uri
        description.text = "Likes: $likes"
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
        postponeEnterTransition()

        Glide.with(this).load(uri)
            .apply(
                RequestOptions().dontTransform()
            )
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            }).into(bigImage)
    }
}
