package com.example.unsplash.model.unsplash;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("NullableProblems")
public class HeaderInterceptor implements Interceptor {

    private String clientId;

    HeaderInterceptor(String clientId) {
        this.clientId = clientId;
    }

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Client-ID " + clientId)
                .build();
        return chain.proceed(request);
    }
}