package com.example.minishop.feature

data class Product(
    val id: Int,
    val title: String,
    val price: String,
    val category: String,
    val description: String,
    val imagePath: String?,
    val isFavorite: Boolean,
    val inCart: Int
)
