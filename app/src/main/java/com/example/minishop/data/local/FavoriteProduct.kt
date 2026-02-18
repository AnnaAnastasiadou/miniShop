package com.example.minishop.data.local

data class FavoriteProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val description: String,
    val imagePath: String
)
