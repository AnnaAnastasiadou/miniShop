package com.example.minishop.core

import javax.inject.Inject

interface AuthTokenProvider {
    fun getToken(): String?
    fun setToken(token: String)
    fun clearToken()
}