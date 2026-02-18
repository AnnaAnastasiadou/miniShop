package com.example.minishop.data.repository.authorization

import com.example.minishop.data.local.datasource.SharedPreferencesDatasource
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.authorization.LoginResponseDto
import javax.inject.Inject

interface AuthRepository {
    suspend fun logIn(username: String, password: String): NetworkResult<LoginResponseDto>
    suspend fun logOut()

}