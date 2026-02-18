package com.example.minishop.data.remote.products

import retrofit2.Response
import retrofit2.http.GET

interface ProductsApi {
    @GET("products")
    suspend fun getProducts() : Response<List<ProductDto>>
}