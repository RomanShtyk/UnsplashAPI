package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Access token.
 */

class AccessToken private constructor(`in`: Parcel) : Parcelable {

    /**
     * accessToken : 091343ce13c8ae780065ecb3b13dc903475dd22cb78a05503c2e0c69c5e98044
     * tokenType : bearer
     * scope : public read_photos write_photos
     * createdAt : 1436544465
     */
    @SerializedName("access_token")
    val accessToken: String? = `in`.readString()
    @SerializedName("token_type")
    val tokenType: String? = `in`.readString()
    @SerializedName("scope")
    val scope: String? = `in`.readString()
    @SerializedName("created_at")
    val createdAt: Int = `in`.readInt()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(accessToken)
        dest.writeString(tokenType)
        dest.writeString(scope)
        dest.writeInt(createdAt)
    }

    companion object CREATOR : Parcelable.Creator<AccessToken> {
        override fun createFromParcel(parcel: Parcel): AccessToken {
            return AccessToken(parcel)
        }

        override fun newArray(size: Int): Array<AccessToken?> {
            return arrayOfNulls(size)
        }
    }
}
