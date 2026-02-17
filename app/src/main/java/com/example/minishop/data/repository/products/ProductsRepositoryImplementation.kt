package com.example.minishop.data.repository.products

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.products.ProductDto
import com.example.minishop.data.remote.products.ProductsApi
import com.example.minishop.data.repository.safeCall
import javax.inject.Inject

class ProductsRepositoryImplementation @Inject constructor(private val productsApi: ProductsApi): ProductsRepository {
    override suspend fun getAllProducts(): NetworkResult<List<ProductDto>> =
        safeCall({ productsApi.getProducts() } )
}