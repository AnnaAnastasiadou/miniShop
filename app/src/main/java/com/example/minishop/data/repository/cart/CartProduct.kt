package com.example.minishop.data.repository.cart

data class CartProduct(
    val id: Int,
    val quantity: Int,
    val title: String,
    val price: Double,
    val totalPrice: Double,
    val imagePath: String?
)
