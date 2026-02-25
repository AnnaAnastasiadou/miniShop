package com.example.minishop.feature.products.details

import com.example.minishop.feature.Product

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val data: Product? = null,
    val error: String? = null
)