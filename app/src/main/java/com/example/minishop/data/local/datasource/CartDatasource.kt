package com.example.minishop.data.local.datasource

import com.example.minishop.data.local.model.CartProductLocal
import kotlinx.coroutines.flow.StateFlow

interface CartDatasource {
    val cartProducts: StateFlow<List<CartProductLocal>>
    fun addItem(cartProduct: CartProductLocal)
    fun removeItem(productId: Int)
    fun updateQuantity(productId: Int, newQuantity: Int)
    fun getQuantityById(productId: Int): Int?
    fun clearCart()
    fun getCartItems(): List<CartProductLocal>
}