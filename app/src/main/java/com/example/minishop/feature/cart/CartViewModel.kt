package com.example.minishop.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.mapper.toCartProduct
import com.example.minishop.data.repository.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

//    private val _uiState = MutableStateFlow(CartProductsUiState())
//    val uiState = _uiState.asStateFlow()
//
//    init {
//        loadProducts()
//    }

//    private fun loadProducts() {
//        viewModelScope.launch {
//            _uiState.update {
//                val cartProducts = cartRepository.getCartProducts().map{productLocal -> productLocal.toCartProduct()}
//                it.copy(
//                    data =
//                        cartProducts
//                )
//            }
//        }
//    }

    val uiState = cartRepository.cartProducts()
        .onStart { cartRepository.getCartProducts() }
        .map { products -> CartProductsUiState(data = products.map { productLocal -> productLocal.toCartProduct() }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CartProductsUiState()
        )

    fun checkout() {}
}