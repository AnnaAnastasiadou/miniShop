package com.example.minishop.data.repository.cart

import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.cart.CheckoutResponseDto
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    suspend fun addItem(cartProduct: CartProductLocal)
    suspend fun removeItem(productId: Int)
    suspend fun getQuantityById(productId: Int): Int
    suspend fun increaseQuantity(productId: Int, quantity: Int)
    suspend fun decreaseQuantity(productId: Int, quantity: Int)
    suspend fun getCartProducts(): List<CartProductLocal>
    fun cartProducts(): StateFlow<List<CartProductLocal>>
    suspend fun clearCart()
    suspend fun checkout(
        date: String,
        products: List<CartProductLocal>
    ): NetworkResult<CheckoutResponseDto>
}