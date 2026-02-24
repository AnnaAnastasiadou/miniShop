package com.example.minishop.feature.products.home

import com.example.minishop.feature.Product

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<String> ?= null
)

data class ProductsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<Product>? = null,
)
data class HomeUiState(
    val categoriesUiState: CategoriesUiState = CategoriesUiState(),
    val productsUiState: ProductsUiState = ProductsUiState()
)
