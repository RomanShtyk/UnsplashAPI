package com.example.unsplash.util

import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory

object Const {
    const val MAIN_FOLDER_NAME = "/UnsplashClient"
    val MAIN_FOLDER =
        getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + MAIN_FOLDER_NAME
    const val WRITE_EXTERNAL_PERMISSION_CODE = 1

    const val UNSPLASH_LOGIN_CALLBACK = "unsplash-auth-callback"
}