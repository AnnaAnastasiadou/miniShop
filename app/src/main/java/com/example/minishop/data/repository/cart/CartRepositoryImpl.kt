package com.example.minishop.data.repository.cart

import com.example.minishop.data.local.datasource.CartDatasource
import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.cart.CartApi
import com.example.minishop.data.remote.cart.CartProductDto
import com.example.minishop.data.remote.cart.CheckoutDto
import com.example.minishop.data.remote.cart.CheckoutResponseDto
import com.example.minishop.data.repository.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi,
    private val cartDatasource: CartDatasource
) : CartRepository {
    override suspend fun addItem(cartProduct: CartProductLocal) {
        withContext(Dispatchers.IO) {
            cartDatasource.addItem(cartProduct)
        }
    }

    override suspend fun removeItem(productId: Int) {
        withContext(Dispatchers.IO) {
            cartDatasource.removeItem(productId)
        }
    }

    override suspend fun getQuantityById(productId: Int): Int = withContext(Dispatchers.IO) {
        return@withContext cartDatasource.getQuantityById(productId) ?: 0
    }


    override suspend fun increaseQuantity(productId: Int, quantity: Int) {
        withContext(Dispatchers.IO) {
            cartDatasource.updateQuantity(productId, quantity + 1)
        }
    }

    override suspend fun decreaseQuantity(productId: Int, quantity: Int) {
        withContext(Dispatchers.IO) {
            cartDatasource.updateQuantity(productId, quantity - 1)
        }
    }

    override suspend fun getCartProducts() {
        withContext(Dispatchers.IO) {
            cartDatasource.getCartItems()
        }
    }

    override fun cartProducts(): StateFlow<List<CartProductLocal>> {
        return cartDatasource.cartProducts
    }

    override suspend fun clearCart() {
        withContext(Dispatchers.IO) {
            cartDatasource.clearCart()
        }
    }


    override suspend fun checkout(
        date: String,
        products: List<CartProductLocal>
    ): NetworkResult<CheckoutResponseDto> =
        safeCall({
            cartApi.checkout(
                request = CheckoutDto(
                    date,
                    products.map { item -> CartProductDto(item.id, item.quantity!!) }
                )
            )
        })
}
