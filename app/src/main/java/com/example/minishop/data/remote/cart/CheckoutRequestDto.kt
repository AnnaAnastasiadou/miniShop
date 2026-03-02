package com.example.minishop.data.remote.cart


data class CheckoutRequestDto(
    val date: String,
    val products: List<CheckoutProductDto>
)
