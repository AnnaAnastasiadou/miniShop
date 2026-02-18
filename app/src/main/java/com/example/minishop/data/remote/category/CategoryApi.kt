package com.example.minishop.data.remote.category

import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET("products/categories/")
    suspend fun getCategories(): Response<List<CategoryDto>>
}