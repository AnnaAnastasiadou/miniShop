package com.example.minishop.data.repository.cart

import com.example.minishop.data.local.model.CartProduct
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.cart.CheckoutResponseDto
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    suspend fun addItem(cartProduct: CartProduct)
    suspend fun removeItem(productId: Int)
    suspend fun increaseQuantity(productId: Int, quantity: Int)
    suspend fun decreaseQuantity(productId: Int, quantity: Int)
    suspend fun getCartProducts()
    fun cartProducts(): StateFlow<List<CartProduct>>
    suspend fun clearCart()
    suspend fun checkout(date: String, products: List<CartProduct>): NetworkResult<CheckoutResponseDto>
}