package com.example.minishop.data.remote.cart


data class CheckoutDto(
    val date: String,
    val products: List<CartProductDto>
)
