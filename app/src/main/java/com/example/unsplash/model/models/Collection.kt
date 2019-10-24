package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.Objects

class Collection() : Parcelable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("published_at")
    @Expose
    var publishedAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("curated")
    @Expose
    var curated: Boolean? = null
    @SerializedName("total_photos")
    @Expose
    var totalPhotos: Int? = null
    @SerializedName("private")
    @Expose
    var private: Boolean? = null
    @SerializedName("share_key")
    @Expose
    var shareKey: String? = null
    @SerializedName("cover_photo")
    @Expose
    var coverPhoto: CoverPhoto? = null
    @SerializedName("user")
    @Expose
    var user: User? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        title = parcel.readString()
        description = parcel.readString()
        publishedAt = parcel.readString()
        updatedAt = parcel.readString()
        curated = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        totalPhotos = parcel.readValue(Int::class.java.classLoader) as? Int
        private = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        shareKey = parcel.readString()
        coverPhoto = parcel.readParcelable(CoverPhoto::class.java.classLoader)
        user = parcel.readParcelable(User::class.java.classLoader)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Collection?
        return id == that!!.id &&
                title == that.title &&
                description == that.description &&
                publishedAt == that.publishedAt &&
                updatedAt == that.updatedAt &&
                curated == that.curated &&
                totalPhotos == that.totalPhotos &&
                private == that.private &&
                shareKey == that.shareKey &&
                coverPhoto == that.coverPhoto &&
                user == that.user
    }

    override fun hashCode(): Int {
        return Objects.hash(id, title, description, publishedAt, updatedAt, curated, totalPhotos, private, shareKey, coverPhoto, user)
    }

    //    public Links getLinks() {
    //        return links;
    //    }
    //
    //    public void setLinks(Links links) {
    //        this.links = links;
    //    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(title)
        dest.writeValue(description)
        dest.writeValue(publishedAt)
        dest.writeValue(updatedAt)
        dest.writeValue(curated)
        dest.writeValue(totalPhotos)
        dest.writeValue(private)
        dest.writeValue(shareKey)
        dest.writeValue(coverPhoto)
        dest.writeValue(user)
        //dest.writeValue(links);
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Collection> {
        override fun createFromParcel(parcel: Parcel): Collection {
            return Collection(parcel)
        }

        override fun newArray(size: Int): Array<Collection?> {
            return arrayOfNulls(size)
        }
    }

}