package com.example.minishop.data.remote

import com.example.minishop.core.AuthSessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: AuthSessionManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.loadToken()

        val request = if (token.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        }

    return chain.proceed(request)
    }


}