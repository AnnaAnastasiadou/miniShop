package com.example.minishop.feature.cart

import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.repository.cart.CartProduct

data class CartProductsUiState(
    val data: List<CartProduct> = emptyList(),
) {
    val totalPrice: Double
        get() = data.sumOf { it.totalPrice}
}

