package com.example.minishop.data.repository.cart

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.cart.CartApi
import com.example.minishop.data.remote.cart.CartProductDto
import com.example.minishop.data.remote.cart.CheckoutDto
import com.example.minishop.data.remote.cart.CheckoutResponseDto
import com.example.minishop.data.repository.safeCall
import com.example.minishop.feature.CartProduct
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartApi: CartApi) : CartRepository {
    override suspend fun checkout(
        date: String,
        products: List<CartProduct>
    ): NetworkResult<CheckoutResponseDto> =
        safeCall({
            cartApi.checkout(
                request = CheckoutDto(
                    date,
                    products.map { item -> CartProductDto(item.productId, item.quantity) }
                )
            )
        })
}
