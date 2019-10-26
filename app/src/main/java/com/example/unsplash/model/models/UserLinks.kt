package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable

/**
 * User links.
 */

class UserLinks(`in`: Parcel) : Parcelable {

    /**
     * self : https://api.unsplash.com/users/mattrobinjones
     * html : http://unsplash.com/@mattrobinjones
     * photos : https://api.unsplash.com/users/mattrobinjones/photos
     * likes : https://api.unsplash.com/users/mattrobinjones/likes
     * portfolio : https://api.unsplash.com/users/mattrobinjones/portfolio
     * following : https://api.unsplash.com/users/mattrobinjones/following
     * followers : https://api.unsplash.com/users/mattrobinjones/followers
     */

    var self: String? = null
    var html: String? = null
    var photos: String? = null
    var likes: String? = null
    var portfolio: String? = null
    var following: String? = null
    var followers: String? = null

    /** <br></br> parcel.  */

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.self)
        dest.writeString(this.html)
        dest.writeString(this.photos)
        dest.writeString(this.likes)
        dest.writeString(this.portfolio)
        dest.writeString(this.following)
        dest.writeString(this.followers)
    }

    init {
        this.self = `in`.readString()
        this.html = `in`.readString()
        this.photos = `in`.readString()
        this.likes = `in`.readString()
        this.portfolio = `in`.readString()
        this.following = `in`.readString()
        this.followers = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<UserLinks> {
        override fun createFromParcel(parcel: Parcel): UserLinks {
            return UserLinks(parcel)
        }

        override fun newArray(size: Int): Array<UserLinks?> {
            return arrayOfNulls(size)
        }
    }
}
