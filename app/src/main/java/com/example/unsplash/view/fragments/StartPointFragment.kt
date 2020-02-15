package com.example.unsplash.view.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.unsplash.R
import com.example.unsplash.model.unsplash.Unsplash
import com.example.unsplash.viewmodel.PhotoViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_startpoint.*
import javax.inject.Inject


class StartPointFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val photoViewModel: PhotoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]
    }

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
            val uri = Uri.parse(Unsplash.UNSPLASH_LOGIN_URI)
                photoViewModel.launchTab(requireContext(), uri)
        }
    }
}
