package com.example.minishop.data.mapper

import com.example.minishop.data.remote.cart.CheckoutProductDto
import com.example.minishop.data.repository.cart.CartProduct
import com.example.minishop.feature.cart.CheckoutProduct

fun CheckoutProductDto.toCheckoutProduct(): CheckoutProduct {
    return CheckoutProduct(
        id = productId,
        quantity = quantity
    )
}