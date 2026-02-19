package com.example.minishop.data.local.datasource

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesDatasourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : SharedPreferencesDatasource {
    private val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private companion object {
        const val TOKEN_NAME = "auth_token"
    }

    override fun saveToken(token: String) {
        sharedPref.edit { putString(TOKEN_NAME, token) }
    }

    override fun getToken(): String? = sharedPref.getString(TOKEN_NAME, null)


    override fun removeToken() {
        sharedPref.edit { remove(TOKEN_NAME) }
    }

}