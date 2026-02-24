package com.example.minishop.feature.products.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.mapper.toProduct
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.category.CategoryDto
import com.example.minishop.data.remote.products.ProductDto
import com.example.minishop.data.repository.category.CategoryRepository
import com.example.minishop.data.repository.products.ProductsRepository
import com.example.minishop.feature.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private var groupedProducts: Map<String, List<Product>> = emptyMap()
    private var allProducts: List<Product>? = null
    private var currentCategoryProducts: List<Product>? = null

    init {
        loadData()
    }


    fun loadData() {
        viewModelScope.launch {
            val categoriesAsync = async { categoryRepository.getCategories() }
            val productAsync = async { productsRepository.getAllProducts() }

            val categoriesRes = categoriesAsync.await()
            val productRes = productAsync.await()


            loadCategories(categoriesRes)
            loadProducts(productRes)
        }
    }

    private fun loadCategories(categoryRes: NetworkResult<List<String>>) {
        _uiState.update {
            it.copy(
                categoriesUiState = it.categoriesUiState.copy(
                    isLoading = true,
                    error = null,
                    data = null
                )
            )
        }
        when (categoryRes) {
            is NetworkResult.Success -> {
                val categories = listOf("all") + categoryRes.data
                _uiState.update {
                    it.copy(
                        categoriesUiState = it.categoriesUiState.copy(
                            isLoading = false,
                            data = categories,
                            error = null
                        )
                    )
                }
            }

            is NetworkResult.Error -> {
                _uiState.update {
                    it.copy(
                        categoriesUiState = it.categoriesUiState.copy(
                            isLoading = false,
                            data = null,
                            error = categoryRes.message
                        )
                    )
                }
            }
        }
    }


    private fun loadProducts(productsRes: NetworkResult<List<ProductDto>>) {
        _uiState.update {
            it.copy(
                productsUiState = it.productsUiState.copy(
                    isLoading = true,
                    data = null,
                    error = null
                )
            )
        }
        when (productsRes) {
            is NetworkResult.Success -> {
                val products = productsRes.data.map { it.toProduct() }
                groupedProducts = products.groupBy { it.category.lowercase() }
                _uiState.update {
                    it.copy(
                        productsUiState = it.productsUiState.copy(
                            isLoading = false,
                            data = products,
                            error = null
                        )
                    )
                }
                allProducts = products
                currentCategoryProducts = products
            }

            is NetworkResult.Error -> {
                _uiState.update {
                    it.copy(
                        productsUiState = it.productsUiState.copy(
                            isLoading = false,
                            data = null,
                            error = productsRes.message
                        )
                    )
                }
            }
        }

    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            is HomeScreenUiEvent.OnCategorySelected -> {
                val categoryLower = event.category.lowercase()
                if (categoryLower == "all") {
                    _uiState.update { it.copy(productsUiState = it.productsUiState.copy(data = allProducts)) }
                    return
                }
                val filteredProducts = groupedProducts[categoryLower] ?: emptyList()
                _uiState.update { it.copy(productsUiState = it.productsUiState.copy(data = filteredProducts)) }
                currentCategoryProducts = filteredProducts
            }

            is HomeScreenUiEvent.OnSearch -> {
                uiState.value.productsUiState.data?.let { data ->
                    val searchedProducts = currentCategoryProducts?.filter {
                        it.title.lowercase().contains(event.query.lowercase())
                    }
                    _uiState.update { it.copy(productsUiState = it.productsUiState.copy(data = searchedProducts)) }
                }
            }

            HomeScreenUiEvent.OnClear -> {
                _uiState.update { it.copy(productsUiState = it.productsUiState.copy(data = allProducts)) }
            }
        }
    }
}