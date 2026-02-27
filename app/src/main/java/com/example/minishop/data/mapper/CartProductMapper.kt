package com.example.minishop.data.mapper

import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.repository.cart.CartProduct

fun CartProductLocal.toCartProduct(): CartProduct {
    return CartProduct(
        id = id,
        quantity = quantity,
        title = title,
        price = price,
        totalPrice =  price * quantity,
        imagePath = imagePath
    )
}