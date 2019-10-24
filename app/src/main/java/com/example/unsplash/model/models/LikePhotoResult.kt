package com.example.unsplash.model.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LikePhotoResult {
    @SerializedName("photo")
    @Expose
    var photo: Photo? = null
    @SerializedName("user")
    @Expose
    var user: User? = null
}
