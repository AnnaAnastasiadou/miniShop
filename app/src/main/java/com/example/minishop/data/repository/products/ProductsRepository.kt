package com.example.minishop.data.repository.products

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.products.ProductDto
import com.example.minishop.data.remote.products.ProductsApi

interface ProductsRepository {
    suspend fun getAllProducts(): NetworkResult<List<ProductDto>>
}