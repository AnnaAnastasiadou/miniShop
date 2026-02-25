package com.example.minishop.feature.products.details

import com.example.minishop.feature.Product

sealed class DetailsScreenUiEvent {
    data object OnToggleFavorite: DetailsScreenUiEvent()
    data class OnIncreaseQuantity(val productId: Int, val quantity: Int): DetailsScreenUiEvent()
    data class OnDecreaseQuantity(val productId: Int, val quantity: Int): DetailsScreenUiEvent()
    data object OnAddToCart: DetailsScreenUiEvent()
    data class OnRemoveFromCart(val productId: Int): DetailsScreenUiEvent()
}
