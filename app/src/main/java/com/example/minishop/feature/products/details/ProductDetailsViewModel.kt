package com.example.minishop.feature.products.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.minishop.data.local.model.CartProductLocal
import com.example.minishop.data.local.model.FavoriteProduct
import com.example.minishop.data.mapper.toProduct
import com.example.minishop.data.repository.cart.CartRepository
import com.example.minishop.data.repository.favorites.FavoritesRepository
import com.example.minishop.data.repository.products.ProductsRepository
import com.example.minishop.feature.RootDestination
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
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle

) : ViewModel() {
    private val productId = checkNotNull(savedStateHandle.get<Int>("productId")) {
        "productIs is missing from SavedStateHandle"
    }
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
            val quantity = cartRepository.getQuantityById(productId)
            val isFav = favoritesRepository.isFavorite(productId)

            if (productDetails != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        data = productDetails.toProduct(isFav = isFav, quantity = quantity),
                        error = null,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        data = null,
                        error = "An Error Occurred"
                    )
                }
            }
        }
    }

    fun onEvent(event: DetailsScreenUiEvent) {
        when (event) {
            is DetailsScreenUiEvent.OnToggleFavorite -> toggleFavorite()
            DetailsScreenUiEvent.OnAddToCart -> addToCart()
            is DetailsScreenUiEvent.OnDecreaseQuantity -> onDecreaseQuantity(
                event.productId,
                event.quantity
            )

            is DetailsScreenUiEvent.OnIncreaseQuantity -> onIncreaseQuantity(
                event.productId,
                event.quantity
            )

            is DetailsScreenUiEvent.OnRemoveFromCart -> onRemoveFromCart(event.productId)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val product = _uiState.value.data ?: return@launch
            if (product.isFavorite) {
                favoritesRepository.removeFavorite(product.id)
                _uiState.update {
                    it.copy(
                        data = product.copy(isFavorite = false)
                    )
                }
            } else {
                favoritesRepository.addFavorite(
                    FavoriteProduct(
                        id = product.id,
                        title = product.title,
                        price = product.price.toDouble(),
                        category = product.category,
                        description = product.description,
                        imagePath = product.imagePath
                    )
                )
                _uiState.update {
                    it.copy(
                        data = product.copy(isFavorite = true)
                    )
                }
            }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val product = _uiState.value.data ?: return@launch
            cartRepository.addItem(
                CartProductLocal(
                    id = product.id,
                    title = product.title,
                    price = product.price.toDouble(),
                    imagePath = product.imagePath,
                    quantity = 1
                )
            )
            _uiState.update { it.copy(data = product.copy(inCart = 1)) }
        }
    }

    fun onRemoveFromCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeItem(productId)
            _uiState.update { it.copy(data = it.data!!.copy(inCart = 0)) }
        }
    }

    fun onIncreaseQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            cartRepository.increaseQuantity(productId, quantity)
            _uiState.update { it.copy(data = it.data!!.copy(inCart = quantity + 1)) }
        }
    }

    fun onDecreaseQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            if (quantity > 1) {
                cartRepository.decreaseQuantity(productId, quantity)
                _uiState.update { it.copy(data = it.data!!.copy(inCart = quantity - 1)) }
            }
        }
    }

}