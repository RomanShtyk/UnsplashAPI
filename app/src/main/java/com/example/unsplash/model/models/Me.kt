package com.example.unsplash.model.models

import com.google.gson.annotations.SerializedName

/**
 * Me
 */

class Me {

    /**
     *
     * "id": "pXhwzz1JtQU",
     * "updated_at": "2016-07-10T11:00:01-05:00",
     * "username": "jimmyexample",
     * "first_name": "James",
     * "last_name": "Example",
     * "twitter_username": "jimmy",
     * "portfolio_url": null,
     * "bio": "The user's bio",
     * "location": "Montreal, Qc",
     * "total_likes": 20,
     * "total_photos": 10,
     * "total_collections": 5,
     * "followed_by_user": false,
     * "downloads": 4321,
     * "uploads_remaining": 4,
     * "instagram_username": "james-example",
     * "email": "jim@example.com",
     * "links": {
     * "self": "https://api.unsplash.com/users/jimmyexample",
     * "html": "https://unsplash.com/jimmyexample",
     * "photos": "https://api.unsplash.com/users/jimmyexample/photos",
     * "likes": "https://api.unsplash.com/users/jimmyexample/likes",
     * "portfolio": "https://api.unsplash.com/users/jimmyexample/portfolio"
     * }
     */
    @SerializedName("id")
    var id: String? = null
    var updated_at: String? = null
    @SerializedName("username")
    var username: String? = null
    var first_name: String? = null
    var last_name: String? = null
    var twitter_username: String? = null
    var portfolio_url: String? = null
    var bio: String? = null
    var location: String? = null
    var total_likes: Int = 0
    var total_photos: Int = 0
    var total_collections: Int = 0
    var followed_by_user: Boolean = false
    var downloads: Int = 0
    var uploads_remaining: Int = 0
    var instagram_username: String? = null
    var email: String? = null
    var links: UserLinks? = null
}
