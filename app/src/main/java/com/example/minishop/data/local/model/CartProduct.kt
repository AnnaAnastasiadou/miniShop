package com.example.minishop.data.local.model

data class CartProduct(
    val id: Int,
    val quantity: Int? = null,
    val title: String,
    val price: Double,
    val imagePath: String?
)