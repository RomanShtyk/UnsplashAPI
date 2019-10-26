package com.example.unsplash.model.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchResults() : Parcelable {

    @SerializedName("total")
    @Expose
    var total: Int? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
    @SerializedName("results")
    @Expose
    var results: List<Photo>? = null

    constructor(parcel: Parcel) : this() {
        total = parcel.readValue(Int::class.java.classLoader) as? Int
        totalPages = parcel.readValue(Int::class.java.classLoader) as? Int
        results = parcel.createTypedArrayList(Photo)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(total)
        dest.writeValue(totalPages)
        dest.writeList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchResults> {
        override fun createFromParcel(parcel: Parcel): SearchResults {
            return SearchResults(parcel)
        }

        override fun newArray(size: Int): Array<SearchResults?> {
            return arrayOfNulls(size)
        }
    }
}
