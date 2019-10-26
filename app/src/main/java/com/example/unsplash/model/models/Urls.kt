package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Urls private constructor(`in`: Parcel) : Parcelable {

    @SerializedName("raw")
    @Expose
    var raw: String? = null
    @SerializedName("full")
    @Expose
    var full: String? = null
    @SerializedName("regular")
    @Expose
    var regular: String? = null
    @SerializedName("small")
    @Expose
    var small: String? = null
    @SerializedName("thumb")
    @Expose
    var thumb: String? = null

    init {
        raw = `in`.readString()
        full = `in`.readString()
        regular = `in`.readString()
        small = `in`.readString()
        thumb = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(raw)
        dest.writeString(full)
        dest.writeString(regular)
        dest.writeString(small)
        dest.writeString(thumb)
    }

    init {
        raw = `in`.readString()
        full = `in`.readString()
        regular = `in`.readString()
        small = `in`.readString()
        thumb = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<Urls> {
        override fun createFromParcel(parcel: Parcel): Urls {
            return Urls(parcel)
        }

        override fun newArray(size: Int): Array<Urls?> {
            return arrayOfNulls(size)
        }
    }
}
