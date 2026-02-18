package com.example.minishop.core

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AuthSessionManager {
    fun token(): String?
    suspend fun logIn(username: String, password: String)
    suspend fun logOut()
    fun isLoggedIn(): Boolean
}