package com.example.unsplash.view.fragments

import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.example.unsplash.util.Const.MAIN_FOLDER
import com.example.unsplash.util.Const.MAIN_FOLDER_NAME
import com.example.unsplash.view.MainActivity
import com.example.unsplash.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_image.*
import org.jetbrains.anko.toast
import java.io.File


class ImageFragment : Fragment() {
    private lateinit var uri: String
    private lateinit var rawUri: String
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
        exitTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
        enterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
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
            rawUri = arguments!!.getString("RAW").toString()
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
        downloadButton.setOnClickListener {
            downloadPhoto(uri, photoId)
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

    private fun downloadPhoto(url: String, name: String) {

        val direct = File("$MAIN_FOLDER/$name")

        if (!direct.exists()) {
            direct.mkdir()
            val dm = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(url)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(name)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + MAIN_FOLDER_NAME + File.separator + name
                )
            dm.enqueue(request)
        } else {
            activity?.toast("Already downloaded")
        }
    }
}
