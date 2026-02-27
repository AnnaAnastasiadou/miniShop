package com.example.minishop.feature.cart

import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.remote.cart.CheckoutResponseDto
import com.example.minishop.data.repository.cart.CartProduct

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val success: CartCheckout? = null,
    val error: String? = null
)

data class CartProductsUiState(
    val data: List<CartProduct> = emptyList(),
    val checkoutUiState: CheckoutUiState = CheckoutUiState()
) {
    val totalPrice: Double
        get() = data.sumOf { it.totalPrice }
}

