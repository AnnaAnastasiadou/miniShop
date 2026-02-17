package com.example.minishop.data.remote.authorization


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ) : Response<LoginResponseDto>
}