package com.example.minishop.data.repository.authorization

import com.example.minishop.core.AuthSessionManager
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.authorization.AuthApi
import com.example.minishop.data.remote.authorization.LoginRequestDto
import com.example.minishop.data.remote.authorization.LoginResponseDto
import com.example.minishop.data.repository.safeCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi, private val sessionManager: AuthSessionManager
) : AuthRepository {
    override suspend fun logIn(
        username: String, password: String
    ): NetworkResult<LoginResponseDto> {

        val result = safeCall {
            authApi.login(
                request = LoginRequestDto(username, password)
            )
        }

        if (result is NetworkResult.Success) {
            sessionManager.saveToken(result.data.token)
        }

        return result
    }

    override suspend fun logOut() = sessionManager.removeToken()


    override suspend fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

}