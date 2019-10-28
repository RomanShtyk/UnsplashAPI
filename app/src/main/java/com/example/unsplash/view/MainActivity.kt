package com.example.unsplash.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.unsplash.R
import com.example.unsplash.model.models.AccessToken
import com.example.unsplash.model.models.Me
import com.example.unsplash.model.unsplash.HeaderInterceptor
import com.example.unsplash.model.unsplash.Unsplash
import com.example.unsplash.model.unsplash.UnsplashAPI
import com.example.unsplash.util.Const.WRITE_EXTERNAL_PERMISSION_CODE
import com.example.unsplash.view.fragments.CollectionFragment
import com.example.unsplash.view.fragments.ListFragment
import com.example.unsplash.view.fragments.StartPointFragment
import kotlinx.android.synthetic.main.main_activity.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private val listFragment = ListFragment()
    private val collectionFragment = CollectionFragment()
    private var active: Fragment = listFragment
    private val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sp = applicationContext.getSharedPreferences("ACCESS_TOKEN", Context.MODE_PRIVATE)
        token = sp.getString("TOKEN", "").toString()
        username = sp.getString("USERNAME", "").toString()
        checkPermissions()
    }


    private fun checkPermissions() {
        when {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_PERMISSION_CODE
            )
            else -> {
                if (intent.data != null) {
                    logIn()
                } else {
                    viewInit()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            checkPermissions()
        } else if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED)) {
            checkPermissions()
        }
        return

    }

    private fun logIn() {
        val uri = intent.data
        val code: String = uri?.getQueryParameter("code").toString()
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(Unsplash.CLIENT_ID)).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Unsplash.BASE_URL_POST)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val unsplashAPI = retrofit.create(UnsplashAPI::class.java)

        val call = unsplashAPI.getAccessToken(
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
                    val unsplashAPITokened =
                        Unsplash.getRetrofitPostInstance(token).create(UnsplashAPI::class.java)
                    val callMe = unsplashAPITokened.meProfile
                    callMe.enqueue(object : Callback<Me> {
                        override fun onResponse(call: Call<Me>, response: Response<Me>) {
                            editor.putString("USERNAME", response.body()?.username)
                            editor.apply()
                            viewInit()
                        }

                        override fun onFailure(call: Call<Me>, t: Throwable) {
                            Log.d("mLog", "ME onFailure: check retrofit base url, try another")
                        }
                    })

                    animateNavigationBar(true)
                    showFab()
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Log.d("mLog", "onFailure: ")
            }
        })
    }

    private fun viewInit() {
        fab.setOnClickListener {
            try {
                val uri = Uri.parse(Unsplash.UNSPLASH_UPLOAD_URL)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                if (intent.resolveActivity(packageManager) != null)
                    startActivity(intent)
                else
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        navigationView.menu.getItem(1).isChecked = true
        navigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_collections -> {
                    navigationView.menu.getItem(0).isChecked = true
                    fm.beginTransaction().hide(active).show(collectionFragment).commit()
                    active = collectionFragment
                }
                R.id.navigation_list -> {
                    navigationView.menu.getItem(1).isChecked = true
                    fm.beginTransaction().hide(active).show(listFragment).commit()
                    active = listFragment
                }
            }
            true
        }
        navigationView.setOnNavigationItemReselectedListener { /*need this*/ }
        if (token != "") {
            fm.beginTransaction().add(R.id.container, collectionFragment, "2")
                .hide(collectionFragment).commit()
            fm.beginTransaction().add(R.id.container, listFragment, "1")
                .commit()
            showFab()
        } else {
            fm.beginTransaction().add(R.id.container, StartPointFragment(), "4").commit()
        }
    }

    fun hideNavBar() {
        animateNavigationBar(false)
        fab.visibility = GONE
    }

    fun showNavBar() {
        navigationView.visibility = VISIBLE
    }

    fun showFab() {
        fab.visibility = VISIBLE
    }

    fun hideFab() {
        fab.visibility = GONE
    }

    private fun animateNavigationBar(show: Boolean) {
        if (navigationView.visibility == VISIBLE && !show || navigationView.visibility != VISIBLE && show) {
            if (show) navigationView.visibility = VISIBLE
            val animate = TranslateAnimation(
                0f,
                0f,
                if (show) navigationView?.height?.toFloat() ?: 0f else 0f,
                if (show) 0f else navigationView?.height?.toFloat() ?: 0f
            )
            animate.duration = 170
            animate.fillAfter = show
            navigationView?.startAnimation(animate)
            if (!show) navigationView.visibility = INVISIBLE
        }
    }


    private fun oneStepBack() {
        val fts = supportFragmentManager.beginTransaction()
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount >= 2) {
            fragmentManager.popBackStackImmediate()
            fts.commit()
        } else {
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        oneStepBack()
    }

    companion object {
        lateinit var token: String
        lateinit var username: String
    }
}
