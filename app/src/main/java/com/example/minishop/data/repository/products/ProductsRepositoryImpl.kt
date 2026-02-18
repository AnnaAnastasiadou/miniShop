package com.example.minishop.data.repository.products

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.products.ProductDto
import com.example.minishop.data.remote.products.ProductsApi
import com.example.minishop.data.repository.safeCall
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsApi: ProductsApi): ProductsRepository {
    private val productList = mutableListOf<ProductDto>()

    override suspend fun getAllProducts(): NetworkResult<List<ProductDto>> {
        val result = safeCall({ productsApi.getProducts() } )
        if (result is NetworkResult.Success) {
            productList.clear()
            productList.addAll(result.data)
        }
        return result
    }

    override suspend fun getProductById(productId: Int): ProductDto? {
        val product = productList.find{it.id == productId}

        return product
    }
}