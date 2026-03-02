package com.example.minishop.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.mapper.toCartProduct
import com.example.minishop.data.mapper.toCheckoutProduct
import com.example.minishop.data.remote.NetworkResult
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

    fun onEvent(event: CartScreenUiEvent) {
        when (event) {
            is CartScreenUiEvent.OnRemoveFromCart -> onRemoveFromCart(event.productId)
            is CartScreenUiEvent.OnDecreaseQuantity -> onDecreaseQuantity(
                event.productId, event.quantity
            )

            is CartScreenUiEvent.OnIncreaseQuantity -> onIncreaseQuantity(
                event.productId, event.quantity
            )

            CartScreenUiEvent.OnCheckout -> onCheckout()
            CartScreenUiEvent.OnCloseCheckoutDialog -> closeCheckoutDialog()
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
            if (quantity > 1) {
                cartRepository.decreaseQuantity(productId, quantity)
            }
        }
    }

    fun onCheckout() {
        viewModelScope.launch {
            _uiState.update { it.copy(checkoutUiState = it.checkoutUiState.copy(isLoading = true)) }

            val response =
                cartRepository.checkout(date = getDateNow(), products = _uiState.value.data)

            when (response) {
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(checkoutUiState = it.checkoutUiState.copy(isLoading = false, success = null,error = "Error while checking out")) }
                }

                is NetworkResult.Success -> {
                    val checkoutData = response.data
                    if (checkoutData.products.isEmpty()) {
                        _uiState.update { it.copy(checkoutUiState = it.checkoutUiState.copy(isLoading = false)) }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(
                            checkoutUiState = it.checkoutUiState.copy(
                                isLoading = false,
                                error = null,
                                success = CartCheckout(
                                    id = checkoutData.id,
                                    date = checkoutData.date,
                                    products = checkoutData.products.map { product -> product.toCheckoutProduct() }
                                )))
                    }
                    cartRepository.clearCart()
                }
            }
        }
    }

    fun closeCheckoutDialog() {
        _uiState.update { it.copy(checkoutUiState = CheckoutUiState()) }
    }
}