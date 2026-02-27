package com.example.minishop.data.remote.cart


data class CartCheckoutDto(
    val date: String,
    val products: List<CheckoutProductDto>
)
