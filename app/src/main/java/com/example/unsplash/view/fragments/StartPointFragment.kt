package com.example.unsplash.view.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.example.unsplash.R
import com.example.unsplash.model.unsplash.Unsplash
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_startpoint.*


class StartPointFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_startpoint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login.setOnClickListener {
            val uri =
                Uri.parse("https://unsplash.com/oauth/authorize" + "?client_id=" + Unsplash.CLIENT_ID + "&redirect_uri=" + Unsplash.REDIRECT_URI + "&response_type=code&scope=public+write_likes+read_user")

            launchTab(requireContext(), uri)
        }
    }

    private fun launchTab(context: Context, uri: Uri) {
        val connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                componentName: ComponentName,
                client: CustomTabsClient
            ) {
                val builder = CustomTabsIntent.Builder()
                val intent = builder.build()
                intent.intent.flags = FLAG_ACTIVITY_NEW_TASK
                client.warmup(0L) // This prevents backgrounding after redirection
                intent.launchUrl(context, uri)
            }

            override fun onServiceDisconnected(name: ComponentName) {}
        }
        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection)
    }
}
