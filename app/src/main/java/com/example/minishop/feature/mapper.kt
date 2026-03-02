package com.example.minishop.feature

import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.repository.cart.CartProduct

fun Product.toCartProduct(): CartProduct {
    return CartProduct(
        id = id,
        quantity = inCart,
        title = title,
        price = price.toDouble(),
        imagePath = imagePath,
        totalPrice = inCart * price.toDouble()
    )
}
