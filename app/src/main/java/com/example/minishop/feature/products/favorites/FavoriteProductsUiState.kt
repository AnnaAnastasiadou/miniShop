package com.example.minishop.feature.products.favorites

import com.example.minishop.data.local.model.FavoriteProduct

data class FavoriteProductsUiState(
    val isLoading: Boolean = false,
    val data: List<FavoriteProduct>? = null,
    val error: String? = null
)
