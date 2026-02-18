package com.example.minishop.core

interface AuthTokenProvider {
    fun getToken(): String?
    fun setToken(token: String)
    fun clearToken()
}