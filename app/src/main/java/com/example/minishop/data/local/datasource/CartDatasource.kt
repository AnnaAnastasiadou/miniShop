package com.example.minishop.data.local.datasource

import com.example.minishop.data.local.model.CartProduct
import kotlinx.coroutines.flow.StateFlow

interface CartDatasource {
    val cartProducts: StateFlow<List<CartProduct>>
    fun addItem(cartProduct: CartProduct)
    fun removeItem(productId: Int)
    fun updateQuantity(productId: Int, newQuantity: Int)
    fun getQuantityById(productId: Int): Int?
    fun clearCart()
    fun getCartItems(): List<CartProduct>
}