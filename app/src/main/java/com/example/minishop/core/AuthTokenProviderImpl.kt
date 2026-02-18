package com.example.minishop.core

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.repository.authorization.AuthRepositoryImpl
import javax.inject.Inject

class AuthTokenProviderImpl @Inject constructor(
    private val authRepo: AuthRepositoryImpl
) : AuthTokenProvider {

    private var cachedToken: String? = null

    override fun getToken(): String? = cachedToken
    override fun setToken(token: String) {
        cachedToken = token
    }

}