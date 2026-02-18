package com.example.minishop.data.repository.favorites

import com.example.minishop.data.local.FavoriteProduct
import com.example.minishop.data.local.datasource.FavoriteProductsDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(private val datasource: FavoriteProductsDatasource) :
    FavoritesRepository {
    override suspend fun addFavorite(product: FavoriteProduct) {
        withContext(Dispatchers.IO) {
            datasource.addFavorite(product)
        }
    }

    override suspend fun removeFavorite(productId: Int) {
        withContext(Dispatchers.IO) {
            datasource.removeFavorite(productId)
        }
    }

    override suspend fun loadFavorites(): List<FavoriteProduct> {
        return withContext(Dispatchers.IO) {
            datasource.getAllFavorites()
        }
    }

    override suspend fun favoriteProducts(): StateFlow<List<FavoriteProduct>> {
        return withContext(Dispatchers.IO) {
            datasource.favorites
        }
    }
}