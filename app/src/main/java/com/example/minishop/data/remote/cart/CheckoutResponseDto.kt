package com.example.minishop.data.remote.cart

data class CheckoutResponseDto(
    val id: Int,
    val date: String,
    val products: List<CartProductDto>
)
