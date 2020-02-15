package com.example.unsplash.viewmodel

import android.app.DownloadManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.unsplash.model.datasource.*
import com.example.unsplash.model.models.*
import com.example.unsplash.model.unsplash.Unsplash
import com.example.unsplash.util.Const
import com.example.unsplash.view.MainActivity.Companion.token
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class PhotoViewModel @Inject constructor() : ViewModel() {
    var list: LiveData<PagedList<Photo>>
    var photoPagedList: LiveData<PagedList<Photo>>
    var favouritesPagedList: LiveData<PagedList<Photo>>
    var searchPagedList: LiveData<PagedList<Photo>>
    var collectionsPagedList: LiveData<PagedList<ColletionPhotos>>
    lateinit var collectionPhotosPagedList: LiveData<PagedList<Photo>>
    var photoLikeChangerObject: MutableLiveData<MyLikeChangerObject>
    private val unsplashAPI by lazy { Unsplash.getRetrofitTokenedAPIInstance(token) }
    @Inject
    lateinit var sp: SharedPreferences

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(10)
        .build()

    init {
        list = LivePagedListBuilder(PhotoDataSourceFactory(), config).build()
        photoPagedList = LivePagedListBuilder(PhotoDataSourceFactory(), config).build()
        searchPagedList = LivePagedListBuilder(SearchDataSourceFactory(""), config).build()
        collectionsPagedList = LivePagedListBuilder(CollectionDataSourceFactory(), config).build()
        favouritesPagedList = LivePagedListBuilder(FavouritesDataSourceFactory(), config).build()

        val my = MyLikeChangerObject("a", false, -1)
        photoLikeChangerObject = MutableLiveData()
        photoLikeChangerObject.value = my
    }

    //TODO refactor this
    fun setLike(id: String) {
        unsplashAPI.likeAPhoto(id).enqueue(object : Callback<LikePhotoResult> {
            override fun onResponse(
                call: Call<LikePhotoResult>,
                response: Response<LikePhotoResult>
            ) {
            }

            override fun onFailure(call: Call<LikePhotoResult>, t: Throwable) {
                Log.d("mLog", "onFailure: set Like")
            }
        })
    }

    fun setDislike(id: String) {
        unsplashAPI.dislikeAPhoto(id).enqueue(object : Callback<LikePhotoResult> {
            override fun onResponse(
                call: Call<LikePhotoResult>,
                response: Response<LikePhotoResult>
            ) {
            }

            override fun onFailure(call: Call<LikePhotoResult>, t: Throwable) {
                Log.d("mLog", "onFailure: set Dislike")
            }
        })
    }

    fun changeLike(myLikeChangerObject: MyLikeChangerObject) {
        photoLikeChangerObject.value = myLikeChangerObject
    }

    fun setQuery(query: String) {
        if (query != "") {
            searchPagedList = LivePagedListBuilder(SearchDataSourceFactory(query), config).build()
        }
    }

    fun setIdCollection(id: String) {
        val collectionPhotosDataSourceFactory = CollectionPhotosDataSourceFactory(id)
        collectionPhotosPagedList =
            LivePagedListBuilder(collectionPhotosDataSourceFactory, config).build()
    }

    fun getList(query: String) {
        list.value?.dataSource?.invalidate()
        list = LivePagedListBuilder(
            if (query != "") SearchDataSourceFactory(query) else PhotoDataSourceFactory(),
            config
        ).build()
    }

    fun launchTab(context: Context, uri: Uri) {
        val connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                componentName: ComponentName,
                client: CustomTabsClient
            ) {
                val builder = CustomTabsIntent.Builder()
                val intent = builder.build()
                intent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                client.warmup(0L) // This prevents backgrounding after redirection
                intent.launchUrl(context, uri)
            }

            override fun onServiceDisconnected(name: ComponentName) {}
        }
        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection)
    }

    fun getAccessToken(code: String) {
        val retrofit = Unsplash.getRetrofitAPIInstance(true)

        val call = retrofit.getAccessToken(
            Unsplash.CLIENT_ID,
            Unsplash.SECRET,
            Unsplash.REDIRECT_URI,
            code,
            "authorization_code"
        )
        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    val editor = sp.edit()
                    editor.putString("TOKEN", response.body()?.accessToken)
                    token = response.body()?.accessToken.toString()
                    editor.apply()
                    val callMe = unsplashAPI.meProfile
                    callMe.enqueue(object : Callback<Me> {
                        override fun onResponse(call: Call<Me>, response: Response<Me>) {
                            editor.putString("USERNAME", response.body()?.username)
                            editor.apply()
                        }

                        override fun onFailure(call: Call<Me>, t: Throwable) {
                            Log.d("mLog", "ME onFailure: check retrofit base url, try another")
                        }
                    })
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Log.d("mLog", "onFailure: ")
            }
        })
    }

    fun downloadPhoto(url: String, name: String, context: Context) {
        val direct = File("${Const.MAIN_FOLDER}/$name")
        if (!direct.exists()) {
            direct.mkdir()
            val dm =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(url)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(name)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + Const.MAIN_FOLDER_NAME + File.separator + name
                )
            dm.enqueue(request)
            context.toast("Downloading...")
        } else {
            context.toast("Already downloaded")
        }
    }
}
