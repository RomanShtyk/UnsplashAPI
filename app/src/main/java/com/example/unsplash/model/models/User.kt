package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class User
constructor(`in`: Parcel) : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("instagram_username")
    @Expose
    var instagramUsername: String? = null

    @SerializedName("twitter_username")
    @Expose
    var twitterUsername: String? = null

    @SerializedName("portfolio_url")
    @Expose
    var portfolioUrl: Any? = null

    @SerializedName("bio")
    @Expose
    var bio: String? = null

    @SerializedName("location")
    @Expose
    var location: String? = null

    @SerializedName("total_likes")
    @Expose
    var totalLikes: Int? = null

    @SerializedName("total_photos")
    @Expose
    var totalPhotos: Int? = null

    @SerializedName("total_collections")
    @Expose
    var totalCollections: Int? = null

    @SerializedName("followed_by_user")
    @Expose
    var followedByUser: Boolean? = null

    @SerializedName("followers_count")
    @Expose
    var followersCount: Int? = null

    @SerializedName("following_count")
    @Expose
    var followingCount: Int? = null

    @SerializedName("downloads")
    @Expose
    var downloads: Int? = null

    init {
        this.id = `in`.readValue(String::class.java.classLoader) as String
        this.updatedAt = `in`.readValue(String::class.java.classLoader) as String
        this.username = `in`.readValue(String::class.java.classLoader) as String
        this.name = `in`.readValue(String::class.java.classLoader) as String
        this.firstName = `in`.readValue(String::class.java.classLoader) as String
        this.lastName = `in`.readValue(String::class.java.classLoader) as String
        this.instagramUsername = `in`.readValue(String::class.java.classLoader) as String
        this.twitterUsername = `in`.readValue(String::class.java.classLoader) as String
        this.portfolioUrl = `in`.readValue(Any::class.java.classLoader)
        this.bio = `in`.readValue(String::class.java.classLoader) as String
        this.location = `in`.readValue(String::class.java.classLoader) as String
        this.totalLikes = `in`.readValue(Int::class.java.classLoader) as Int
        this.totalPhotos = `in`.readValue(Int::class.java.classLoader) as Int
        this.totalCollections = `in`.readValue(Int::class.java.classLoader) as Int
        this.followedByUser = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        this.followersCount = `in`.readValue(Int::class.java.classLoader) as Int
        this.followingCount = `in`.readValue(Int::class.java.classLoader) as Int
        this.downloads = `in`.readValue(Int::class.java.classLoader) as Int
    }


    //    public ProfileImage getProfileImage() {
    //        return profileImage;
    //    }
    //
    //    public void setProfileImage(ProfileImage profileImage) {
    //        this.profileImage = profileImage;
    //    }
    //
    //    public Badge getBadge() {
    //        return badge;
    //    }
    //
    //    public void setBadge(Badge badge) {
    //        this.badge = badge;
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
    //    public List<CurrentUserCollection> getCurrentUserCollections() {
    //        return currentUserCollections;
    //    }
    //
    //    public void setCurrentUserCollections(List<CurrentUserCollection> currentUserCollections) {
    //        this.currentUserCollections = currentUserCollections;
    //    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(updatedAt)
        dest.writeValue(username)
        dest.writeValue(name)
        dest.writeValue(firstName)
        dest.writeValue(lastName)
        dest.writeValue(instagramUsername)
        dest.writeValue(twitterUsername)
        dest.writeValue(portfolioUrl)
        dest.writeValue(bio)
        dest.writeValue(location)
        dest.writeValue(totalLikes)
        dest.writeValue(totalPhotos)
        dest.writeValue(totalCollections)
        dest.writeValue(followedByUser)
        dest.writeValue(followersCount)
        dest.writeValue(followingCount)
        dest.writeValue(downloads)
        //        dest.writeValue(profileImage);
        //        dest.writeValue(badge);
        //        dest.writeValue(links);
        //        dest.writeList(currentUserCollections);
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}