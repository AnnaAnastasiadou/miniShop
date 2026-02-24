package com.example.minishop.feature.products.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.mapper.toProduct
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.repository.favorites.FavoritesRepository
import com.example.minishop.data.repository.products.ProductsRepository
import com.example.minishop.feature.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val productId: Int = savedStateHandle.get<Int>("productId") ?: -1
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProductDetails()
    }

    private fun getProductDetails() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, data = null, error = null)
            }
            val productDetails = productsRepository.getProductById(productId)

            if (productDetails != null) {
                _uiState.update { it.copy(isLoading = false, data = productDetails.toProduct(), error = null,) }
            } else {
                _uiState.update { it.copy(isLoading = false, data = null, error = "An Error Occurred") }
            }
        }
    }
}