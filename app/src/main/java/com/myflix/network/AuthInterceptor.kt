package com.myflix.network

import com.myflix.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithAuth = originalRequest.newBuilder()
            // Ensure this matches the field in your build.gradle.kts
            .header("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()
        return chain.proceed(requestWithAuth)
    }
}
