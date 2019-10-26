package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CoverPhoto() : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String? = null
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
    @SerializedName("user")
    @Expose
    var user: User? = null
    @SerializedName("urls")
    @Expose
    var urls: Urls? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        width = parcel.readValue(Int::class.java.classLoader) as? Int
        height = parcel.readValue(Int::class.java.classLoader) as? Int
        color = parcel.readString()
        likes = parcel.readValue(Int::class.java.classLoader) as? Int
        likedByUser = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        user = parcel.readParcelable(User::class.java.classLoader)
        urls = parcel.readParcelable(Urls::class.java.classLoader)
    }

    fun withId(id: String): CoverPhoto {
        this.id = id
        return this
    }

    fun withWidth(width: Int?): CoverPhoto {
        this.width = width
        return this
    }

    fun withHeight(height: Int?): CoverPhoto {
        this.height = height
        return this
    }

    fun withColor(color: String): CoverPhoto {
        this.color = color
        return this
    }

    fun withLikes(likes: Int?): CoverPhoto {
        this.likes = likes
        return this
    }

    fun withLikedByUser(likedByUser: Boolean?): CoverPhoto {
        this.likedByUser = likedByUser
        return this
    }

    fun withUser(user: User): CoverPhoto {
        this.user = user
        return this
    }

    fun withUrls(urls: Urls): CoverPhoto {
        this.urls = urls
        return this
    }

    //    public List<Category> getCategories() {
    //        return categories;
    //    }
    //
    //    public void setCategories(List<Category> categories) {
    //        this.categories = categories;
    //    }
    //
    //    public CoverPhoto withCategories(List<Category> categories) {
    //        this.categories = categories;
    //        return this;
    //    }
    //
    //    public Links getLinks() {
    //        return links;
    //    }
    //
    //    public void setLinks(Links links) {
    //        this.links = links;
    //    }
    //
    //    public CoverPhoto withLinks(Links links) {
    //        this.links = links;
    //        return this;
    //    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(width)
        dest.writeValue(height)
        dest.writeValue(color)
        dest.writeValue(likes)
        dest.writeValue(likedByUser)
        dest.writeValue(user)
        dest.writeValue(urls)
        //        dest.writeList(categories);
        //        dest.writeValue(links);
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<CoverPhoto> {
        override fun createFromParcel(parcel: Parcel): CoverPhoto {
            return CoverPhoto(parcel)
        }

        override fun newArray(size: Int): Array<CoverPhoto?> {
            return arrayOfNulls(size)
        }
    }
}
