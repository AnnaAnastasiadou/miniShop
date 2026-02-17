package com.example.minishop.data.repository.favorites

import com.example.minishop.data.local.FavoriteProduct
import kotlinx.coroutines.flow.StateFlow

interface FavoritesRepository {
    suspend fun addFavorite(product: FavoriteProduct)
    suspend fun removeFavorite(productId: Int)
    suspend fun loadFavorites(): List<FavoriteProduct>
    suspend fun favoriteProducts() : StateFlow<List<FavoriteProduct>>
}