package com.example.unsplash.view

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.unsplash.R
import com.example.unsplash.model.unsplash.Unsplash
import com.example.unsplash.view.fragments.CollectionFragment
import com.example.unsplash.view.fragments.ListFragment
import com.example.unsplash.view.fragments.StartPointFragment
import com.example.unsplash.viewmodel.PhotoViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import java.util.concurrent.Executor
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var sp: SharedPreferences
    private val listFragment = ListFragment()
    private val collectionFragment = CollectionFragment()
    private var active: Fragment = listFragment
    private val fm = supportFragmentManager
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val photoViewModel: PhotoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        token = sp.getString("TOKEN", "").toString()
        username = sp.getString("USERNAME", "").toString()
        if (intent.data != null) {
            logIn()
        } else {
            startInit()
        }
    }

    private fun getMainThreadExecutor(): MainThreadExecutor {
        return MainThreadExecutor()
    }

    private class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())

        override fun execute(r: Runnable) {
            handler.post(r)
        }
    }

    private fun logIn() {
        val uri = intent.data
        val code: String = uri?.getQueryParameter("code").toString()
        photoViewModel.getAccessToken(code)
        startInit()
    }

    private fun startInit() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            //TODO show this on oneplus 6t
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Log in using your biometric credential")
                .setSubtitle("Or just use your PIN")
                .setDescription("Set the description to display")
                //.setNegativeButtonText("Cancel")
                .setDeviceCredentialAllowed(true)
                .build()
            val biometricPrompt =
                BiometricPrompt(
                    this,
                    getMainThreadExecutor(),
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            viewInit()
                        }

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(applicationContext, "Authentication failed",
                                Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
            biometricPrompt.authenticate(promptInfo)
        } else {
            viewInit()
        }
    }

    private fun viewInit() {
        //TODO refactor this
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
                .hide(collectionFragment).add(R.id.container, listFragment, "1")
                .commitAllowingStateLoss()//need this because of  exceptions
            animateNavigationBar(true)
            showFab()
        } else {
            fm.beginTransaction().add(R.id.container, StartPointFragment(), "4")
                .commitAllowingStateLoss()//need this because of  exceptions
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
//            if (show) navigationView.visibility = VISIBLE
//            val animate = TranslateAnimation(
//                0f,
//                0f,
//                if (show) navigationView?.height?.toFloat() ?: 0f else 0f,
//                if (show) 0f else navigationView?.height?.toFloat() ?: 0f
//            )
//            animate.duration = 170
//            animate.fillAfter = show
//            navigationView?.startAnimation(animate)
//            if (!show) navigationView.visibility = INVISIBLE
            if (show) {
                fadeInAnimation(navigationView)
            } else {
                fadeOutAnimation(navigationView)
            }
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

    private fun fadeInAnimation(view: View) {
        val fadeIn: Animation = AlphaAnimation(0F, 1F)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 170L
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(fadeIn)
    }

    private fun fadeOutAnimation(view: View) {
        val fadeOut: Animation = AlphaAnimation(1F, 0F)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.startOffset = 170L
        fadeOut.duration = 170L
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(fadeOut)
    }

    override fun onBackPressed() {
        oneStepBack()
    }

    companion object {
        lateinit var token: String
        lateinit var username: String
    }
}
