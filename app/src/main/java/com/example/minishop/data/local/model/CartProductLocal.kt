package com.example.minishop.data.local.model

data class CartProductLocal(
    val id: Int,
    val quantity: Int,
    val title: String,
    val price: Double,
    val imagePath: String?
)