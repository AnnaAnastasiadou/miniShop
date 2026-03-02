package com.example.minishop.data.remote.cart

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CartApi {
    @POST("carts/")
    suspend fun checkout(
        @Body request: CheckoutRequestDto
    ) : Response<CheckoutResponseDto>
}