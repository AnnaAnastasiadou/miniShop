package com.example.minishop.data.repository.category

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.category.CategoryDto

interface CategoryRepository {
    suspend fun getCategories(): NetworkResult<List<CategoryDto>>
}