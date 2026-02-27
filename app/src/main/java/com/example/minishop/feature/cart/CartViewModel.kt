package com.example.minishop.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.local.model.CartProductLocal
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

    private val _uiState = MutableStateFlow(CartProductsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { cartRepository.getCartProducts() }
        viewModelScope.launch {
            cartRepository.cartProducts().collect { list ->
                _uiState.update {
                    it.copy(data = list.map { productLocal -> productLocal.toCartProduct() })
                }
            }
        }
    }

//    val uiState = cartRepository.cartProducts()
//        .onStart { cartRepository.getCartProducts() }
//        .map { products -> CartProductsUiState(data = products.map { productLocal -> productLocal.toCartProduct() }) }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = CartProductsUiState()
//        )


    fun onEvent(event: CartScreenUiEvent) {
        when (event) {
            is CartScreenUiEvent.OnRemoveFromCart -> onRemoveFromCart(event.productId)
            is CartScreenUiEvent.OnDecreaseQuantity -> onDecreaseQuantity(
                event.productId, event.quantity
            )

            is CartScreenUiEvent.OnIncreaseQuantity -> onIncreaseQuantity(
                event.productId, event.quantity
            )
        }
    }

    fun onRemoveFromCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeItem(productId)
        }
    }

    fun onIncreaseQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            cartRepository.increaseQuantity(productId, quantity)
        }
    }

    fun onDecreaseQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            cartRepository.decreaseQuantity(productId, quantity)
        }
    }

    fun checkout() {}

}