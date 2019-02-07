package com.example.unsplash.model.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikePhotoResult {
    @SerializedName("photo")
    @Expose
    public Photo photo;
    @SerializedName("user")
    @Expose
    public User user;
}
