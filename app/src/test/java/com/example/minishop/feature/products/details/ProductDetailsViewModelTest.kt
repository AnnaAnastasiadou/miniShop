package com.example.minishop.feature.products.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.minishop.data.cartProductLocale
import com.example.minishop.data.favoriteProduct
import com.example.minishop.data.productDetails
import com.example.minishop.data.productDetailsDto
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.repository.cart.CartRepository
import com.example.minishop.data.repository.favorites.FavoritesRepository
import com.example.minishop.data.repository.products.ProductsRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {
    private val favoritesRepository: FavoritesRepository = mockk()
    private val productsRepository: ProductsRepository = mockk()
    private val cartRepository: CartRepository = mockk()
    private val savedStateHandle = SavedStateHandle(mapOf("productId" to 1))
    private lateinit var viewModel: ProductDetailsViewModel
    private val testDispatcher = StandardTestDispatcher()


    private fun createViewModel() = ProductDetailsViewModel(
        favoritesRepository,
        productsRepository,
        cartRepository,
        savedStateHandle
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUiState initial state verification`() {
        // Verify that the initial state of uiState has isLoading=true, data=null, and error=null before repository results return.
        // TODO implement test
    }

    @Test
    fun `getProductDetails success mapping`() = runTest {
        // Verify that uiState updates with correct product data, isFav status, and quantity when productsRepository returns a valid object.
//        viewModel = ProductDetailsViewModel(
//            favoritesRepository,
//            productsRepository,
//            cartRepository,
//            savedStateHandle
//        )
        coEvery { productsRepository.getProductById(1) } returns productDetailsDto
        coEvery { cartRepository.getQuantityById(1) } returns 0
        coEvery { favoritesRepository.isFavorite(1) } returns false
        viewModel = createViewModel()
        viewModel.uiState.test {
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = true, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails, error = null),
                awaitItem()
            )
        }
    }

    @Test
    fun `getProductDetails failure handling`() {
        // Verify that uiState updates with isLoading=false and an error message when productsRepository returns null.
        // TODO implement test
    }

    @Test
    fun `onEvent OnToggleFavorite routing`() {
        // Verify that calling onEvent with OnToggleFavorite triggers the toggleFavorite logic.
    }

    @Test
    fun `onEvent OnAddToCart routing`() {
        // Verify that calling onEvent with OnAddToCart triggers the addToCart logic.
        // TODO implement test
    }

    @Test
    fun `onEvent OnIncreaseQuantity routing`() {
        // Verify that calling onEvent with OnIncreaseQuantity passes the correct productId and quantity to the handler.
        // TODO implement test
    }

    @Test
    fun `onEvent OnDecreaseQuantity routing`() {
        // Verify that calling onEvent with OnDecreaseQuantity passes the correct productId and quantity to the handler.
        // TODO implement test
    }

    @Test
    fun `onEvent OnRemoveFromCart routing`() {
        // Verify that calling onEvent with OnRemoveFromCart triggers the removal logic with the correct ID.
        // TODO implement test
    }

    @Test
    fun `toggleFavorite add to favorites`() = runTest {
        // Check if favoritesRepository.addFavorite is called and uiState is updated to isFavorite=true when current state is false.
        coEvery { productsRepository.getProductById(1) } returns productDetailsDto
        coEvery { cartRepository.getQuantityById(1) } returns 0
        coEvery { favoritesRepository.isFavorite(1) } returns false
        coEvery { favoritesRepository.addFavorite(favoriteProduct) } returns Unit
        viewModel = createViewModel()
        viewModel.uiState.test {
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = true, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails, error = null),
                awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnToggleFavorite)
            assertEquals(
                ProductDetailsUiState(
                    isLoading = false,
                    error = null,
                    data = productDetails.copy(isFavorite = true)
                ), awaitItem()
            )
        }
    }

    @Test
    fun `toggleFavorite remove from favorites`() {
        // Check if favoritesRepository.removeFavorite is called and uiState is updated to isFavorite=false when current state is true.
        // TODO implement test
    }

    @Test
    fun `toggleFavorite null data guard`() {
        // Ensure toggleFavorite does nothing and does not crash if uiState.value.data is null.
        // TODO implement test
    }

    @Test
    fun `addToCart success operation`() = runTest {
        // Verify cartRepository.addItem is called with correct parameters and uiState.data.inCart is updated to 1.
        coEvery { productsRepository.getProductById(1) } returns productDetailsDto
        coEvery { cartRepository.getQuantityById(1) } returns 0
        coEvery { favoritesRepository.isFavorite(1) } returns false
        coEvery { favoritesRepository.addFavorite(favoriteProduct) } returns Unit
        coEvery { cartRepository.addItem(cartProductLocale) } returns Unit
        viewModel = createViewModel()
        viewModel.uiState.test {
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = true, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails, error = null),
                awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnAddToCart)
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails.copy(inCart = 1), error = null),
                awaitItem()
            )
        }
    }

    @Test
    fun `addToCart null data guard`() {
        // Ensure addToCart does nothing and does not crash if uiState.value.data is null.
        // TODO implement test
    }

    @Test
    fun `onRemoveFromCart logic verification`() {
        // Verify cartRepository.removeItem is called and uiState.data.inCart is updated to 0.
        // TODO implement test
    }

    @Test
    fun `onRemoveFromCart null data exception safety`() {
        // Test behavior/crash potential when onRemoveFromCart is called while uiState.data is null (forced null assertion check).
        // TODO implement test
    }

    @Test
    fun `onIncreaseQuantity logic verification`() = runTest{
        // Verify cartRepository.increaseQuantity is called and uiState.data.inCart is updated to current quantity + 1.
        coEvery { productsRepository.getProductById(1) } returns productDetailsDto
        coEvery { cartRepository.getQuantityById(1) } returns 0
        coEvery { favoritesRepository.isFavorite(1) } returns false
        coEvery { cartRepository.increaseQuantity(1, 1) } returns Unit
        coEvery { cartRepository.increaseQuantity(1, 2) } returns Unit


        viewModel = createViewModel()
        viewModel.uiState.test {
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = true, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails, error = null),
                awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnIncreaseQuantity(1, 1))
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails.copy(inCart = 2), error = null),
                awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnIncreaseQuantity(1, 2))
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails.copy(inCart = 3), error = null),
                awaitItem()
            )
        }
    }

    @Test
    fun `onIncreaseQuantity boundary check`() {
        // Verify behavior when quantity is at Int.MAX_VALUE to check for integer overflow in the UI state update.
        // TODO implement test
    }

    @Test
    fun `onDecreaseQuantity valid decrease`() {
        // Verify cartRepository.decreaseQuantity is called and uiState.data.inCart is updated to current quantity - 1 when quantity > 1.
        // TODO implement test
    }

    @Test
    fun `onDecreaseQuantity minimum threshold`() {
        // Verify that if quantity is 1, decreaseQuantity is NOT called and uiState remains unchanged.
        // TODO implement test
    }

    @Test
    fun `onDecreaseQuantity negative input handling`() {
        // Ensure the function handles or ignores negative quantity inputs correctly due to the > 1 condition.
        // TODO implement test
    }

    @Test
    fun `Race condition check for rapid events`() = runTest{
        // Test if multiple rapid calls to toggleFavorite or onIncreaseQuantity handle state updates consistently without losing data.
        coEvery { productsRepository.getProductById(1) } returns productDetailsDto
        coEvery { cartRepository.getQuantityById(1) } returns 0
        coEvery { favoritesRepository.isFavorite(1) } returns false
        coEvery { favoritesRepository.addFavorite(favoriteProduct) } returns Unit
        coEvery { favoritesRepository.removeFavorite(1) } returns Unit
        viewModel = createViewModel()
        viewModel.uiState.test {
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = true, data = null, error = null),
                awaitItem()
            )
            assertEquals(
                ProductDetailsUiState(isLoading = false, data = productDetails, error = null),
                awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnToggleFavorite)
            assertEquals(
                ProductDetailsUiState(
                    isLoading = false,
                    error = null,
                    data = productDetails.copy(isFavorite = true)
                ), awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnToggleFavorite)
            assertEquals(
                ProductDetailsUiState(
                    isLoading = false,
                    error = null,
                    data = productDetails
                ), awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnToggleFavorite)
            assertEquals(
                ProductDetailsUiState(
                    isLoading = false,
                    error = null,
                    data = productDetails.copy(isFavorite = true)
                ), awaitItem()
            )
            viewModel.onEvent(DetailsScreenUiEvent.OnToggleFavorite)
            assertEquals(
                ProductDetailsUiState(
                    isLoading = false,
                    error = null,
                    data = productDetails
                ), awaitItem()
            )
        }
    }

}