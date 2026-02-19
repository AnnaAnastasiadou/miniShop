package com.example.minishop.data.repository.authorization

import com.example.minishop.core.AuthTokenProvider
import com.example.minishop.data.local.datasource.SharedPreferencesDatasource
import com.example.minishop.data.local.datasource.SharedPreferencesDatasourceImpl
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.authorization.AuthApi
import com.example.minishop.data.remote.authorization.LoginRequestDto
import com.example.minishop.data.remote.authorization.LoginResponseDto
import com.example.minishop.data.repository.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sharedPreferencesDatasource: SharedPreferencesDatasource,
    private val tokenProvider: AuthTokenProvider
) : AuthRepository {
    override suspend fun logIn(
        username: String, password: String
    ): NetworkResult<LoginResponseDto> =
        withContext(Dispatchers.IO) {

            val result = safeCall {
                authApi.login(
                    request = LoginRequestDto(username, password)
                )
            }

            if (result is NetworkResult.Success) {
                sharedPreferencesDatasource.saveToken(result.data.token)
                tokenProvider.setToken(result.data.token)
            }
            result
        }


    override suspend fun logOut() = withContext(Dispatchers.IO) {
        sharedPreferencesDatasource.removeToken()
        tokenProvider.clearToken()
    }

    override fun isLoggedIn(): Boolean {
        var token = tokenProvider.getToken()
        if (token == null) {
            sharedPreferencesDatasource.getToken()?.let {
                token = it
                tokenProvider.setToken(token)
            }
        }
        return token != null
    }
}
