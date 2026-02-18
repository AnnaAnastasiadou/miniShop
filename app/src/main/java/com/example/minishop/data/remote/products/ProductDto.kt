package com.example.minishop.data.remote.products

data class ProductDto(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val description: String,
    val image: String
)