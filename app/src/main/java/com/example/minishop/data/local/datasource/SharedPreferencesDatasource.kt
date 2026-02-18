package com.example.minishop.data.local.datasource

interface SharedPreferencesDatasource {
    fun saveToken(token: String)
    fun getToken(): String?
    fun removeToken()
}