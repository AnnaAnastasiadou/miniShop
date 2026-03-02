package com.example.minishop.feature.cart

sealed class CartScreenUiEvent {
    data class OnIncreaseQuantity(val productId: Int, val quantity: Int): CartScreenUiEvent()
    data class OnDecreaseQuantity(val productId: Int, val quantity: Int): CartScreenUiEvent()
    data class OnRemoveFromCart(val productId: Int): CartScreenUiEvent()
    data object OnCheckout: CartScreenUiEvent()
    data object OnCloseCheckoutDialog: CartScreenUiEvent()
}