package com.example.minishop.di

import com.example.minishop.data.repository.authorization.AuthRepository
import com.example.minishop.data.repository.authorization.AuthRepositoryImpl
import com.example.minishop.data.repository.cart.CartRepository
import com.example.minishop.data.repository.cart.CartRepositoryImpl
import com.example.minishop.data.repository.category.CategoryRepository
import com.example.minishop.data.repository.category.CategoryRepositoryImpl
import com.example.minishop.data.repository.favorites.FavoritesRepository
import com.example.minishop.data.repository.favorites.FavoritesRepositoryImpl
import com.example.minishop.data.repository.products.ProductsRepository
import com.example.minishop.data.repository.products.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ) : CartRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ) : CategoryRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ) : FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindProductsRepository(
        productsRepositoryImpl: ProductsRepositoryImpl
    ) : ProductsRepository

}