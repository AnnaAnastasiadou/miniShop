package com.example.minishop.feature.cart

import com.example.minishop.data.repository.cart.CartProduct

data class CartCheckout (
    val id: Int,
    val date: String,
    val products: List<CheckoutProduct>
)
