package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result() : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("created-at")
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
    @SerializedName("current_user_collections")
    @Expose
    var currentUserCollections: List<Any>? = null
    @SerializedName("urls")
    @Expose
    var urls: Urls? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
        width = parcel.readValue(Int::class.java.classLoader) as? Int
        height = parcel.readValue(Int::class.java.classLoader) as? Int
        color = parcel.readString()
        likes = parcel.readValue(Int::class.java.classLoader) as? Int
        likedByUser = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        urls = parcel.readParcelable(Urls::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(createdAt)
        dest.writeValue(updatedAt)
        dest.writeValue(width)
        dest.writeValue(height)
        dest.writeValue(color)
        dest.writeValue(likes)
        dest.writeValue(likedByUser)
        dest.writeList(currentUserCollections)
        dest.writeValue(urls)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }
}
