package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Photo private constructor(`in`: Parcel) : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
    @SerializedName("height")
    @Expose
    var height: Int? = null
    @SerializedName("color")
    @Expose
    var color: String? = null
    @SerializedName("likes")
    @Expose
    var likes: Int? = null
    @SerializedName("liked_by_user")
    @Expose
    var likedByUser: Boolean? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("urls")
    @Expose
    var urls: Urls? = null

    init {
        id = `in`.readString()
        createdAt = `in`.readString()
        updatedAt = `in`.readString()
        if (`in`.readByte().toInt() == 0) {
            width = null
        } else {
            width = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            height = null
        } else {
            height = `in`.readInt()
        }
        color = `in`.readString()
        if (`in`.readByte().toInt() == 0) {
            likes = null
        } else {
            likes = `in`.readInt()
        }
        val tmpLikedByUser = `in`.readByte()
        likedByUser = if (tmpLikedByUser.toInt() == 0) null else tmpLikedByUser.toInt() == 1
        description = `in`.readString()
        urls = `in`.readParcelable(Urls::class.java.classLoader)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(createdAt)
        dest.writeString(updatedAt)
        if (width == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(width!!)
        }
        if (height == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(height!!)
        }
        dest.writeString(color)
        if (likes == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(likes!!)
        }
        dest.writeByte((if (likedByUser == null) 0 else if (likedByUser as Boolean) 1 else 2).toByte())
        dest.writeString(description)
        dest.writeParcelable(urls, flags)
    }

    init {
        id = `in`.readString()
        createdAt = `in`.readString()
        updatedAt = `in`.readString()
        width = `in`.readValue(Int::class.java.classLoader) as? Int
        height = `in`.readValue(Int::class.java.classLoader) as? Int
        color = `in`.readString()
        likes = `in`.readValue(Int::class.java.classLoader) as? Int
        likedByUser = `in`.readValue(Boolean::class.java.classLoader) as? Boolean
        description = `in`.readString()
        urls = `in`.readParcelable(Urls::class.java.classLoader)
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}