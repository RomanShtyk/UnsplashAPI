package com.example.unsplash.model.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Access token.
 * */

public class AccessToken implements Parcelable {

    /**
     * access_token : 091343ce13c8ae780065ecb3b13dc903475dd22cb78a05503c2e0c69c5e98044
     * token_type : bearer
     * scope : public read_photos write_photos
     * created_at : 1436544465
     */
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("token_type")
    private String token_type;
    @SerializedName("scope")
    private String scope;
    @SerializedName("created_at")
    private int created_at;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public int getCreated_at() {
        return created_at;
    }

    private AccessToken(Parcel in) {
        access_token = in.readString();
        token_type = in.readString();
        scope = in.readString();
        created_at = in.readInt();
    }

    public static final Creator<AccessToken> CREATOR = new Creator<AccessToken>() {
        @Override
        public AccessToken createFromParcel(Parcel in) {
            return new AccessToken(in);
        }

        @Override
        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(token_type);
        dest.writeString(scope);
        dest.writeInt(created_at);
    }
}
