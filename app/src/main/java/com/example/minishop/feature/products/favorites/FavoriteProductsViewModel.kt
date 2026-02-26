package com.example.minishop.feature.products.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.local.datasource.FavoriteProductsDatasource
import com.example.minishop.data.repository.favorites.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteProductsViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(FavoriteProductsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch{
            favoritesRepository.loadFavorites()
        }
        viewModelScope.launch{
            favoritesRepository.favoriteProducts().collect{updatedList ->
                _uiState.update { it.copy(data = updatedList) }
            }
        }
    }

}