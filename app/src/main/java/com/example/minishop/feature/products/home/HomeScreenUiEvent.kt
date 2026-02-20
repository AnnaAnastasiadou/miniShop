package com.example.minishop.feature.products.home

sealed class HomeScreenUiEvent {
    data class OnCategorySelected(val category: String): HomeScreenUiEvent()
    data class OnSearch(val query: String): HomeScreenUiEvent()
    data object OnClear: HomeScreenUiEvent()
}