package com.example.unsplash.model.unsplash

import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptorPost internal constructor(private val token: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        return chain.proceed(request)
    }
}
