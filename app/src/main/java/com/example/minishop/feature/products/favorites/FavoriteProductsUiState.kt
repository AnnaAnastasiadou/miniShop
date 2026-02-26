package com.example.minishop.feature.products.favorites

import com.example.minishop.data.local.model.FavoriteProduct

data class FavoriteProductsUiState(
    val data: List<FavoriteProduct> = emptyList()
)
