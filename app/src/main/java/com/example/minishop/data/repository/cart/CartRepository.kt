package com.example.minishop.data.repository.cart

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.cart.CheckoutResponseDto
import com.example.minishop.feature.CartProduct

interface CartRepository {
    suspend fun checkout(date: String, products: List<CartProduct>): NetworkResult<CheckoutResponseDto>
}