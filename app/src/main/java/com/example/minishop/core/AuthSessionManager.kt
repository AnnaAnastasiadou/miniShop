package com.example.minishop.core

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthSessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private companion object {
        const val TOKEN_NAME = "auth_token"
    }

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()
    private var cachedToken: String? = null

    fun saveToken(token: String) {
        sharedPref.edit { putString(TOKEN_NAME, token) }
        _isLoggedIn.value = true
        cachedToken = token
    }

    fun loadToken(): String? = cachedToken


    fun removeToken() {
        sharedPref.edit { remove(TOKEN_NAME) }
        _isLoggedIn.value = false
        cachedToken = null
    }

    fun isLoggedIn(): Boolean = isLoggedIn.value

}