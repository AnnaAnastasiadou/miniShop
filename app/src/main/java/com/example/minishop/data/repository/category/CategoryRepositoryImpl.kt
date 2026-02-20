package com.example.minishop.data.repository.category

import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.category.CategoryApi
import com.example.minishop.data.remote.category.CategoryDto
import com.example.minishop.data.repository.safeCall
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryApi: CategoryApi) :
    CategoryRepository {
    override suspend fun getCategories(): NetworkResult<List<String>> =
        safeCall { categoryApi.getCategories() }
}