package com.example.minishop.core

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.repository.authorization.AuthRepositoryImpl
import javax.inject.Inject

class AuthSessionManagerImpl @Inject constructor(
    private val authRepo: AuthRepositoryImpl
) : AuthSessionManager {

    private var cachedToken: String? = null

    override fun token(): String? = cachedToken

    override suspend fun logIn(username: String, password: String) {
        val response = authRepo.logIn(username, password)
        if (response is NetworkResult.Success) {
            cachedToken = response.data.token
        }
    }

    override suspend fun logOut() {
        authRepo.logOut()
        cachedToken = null
    }

    override fun isLoggedIn(): Boolean = cachedToken!= null
}