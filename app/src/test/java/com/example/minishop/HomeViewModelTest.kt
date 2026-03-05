package com.example.minishop

import app.cash.turbine.test
import com.example.minishop.data.categories
import com.example.minishop.data.categoriesDto
import com.example.minishop.data.electronicProducts
import com.example.minishop.data.products
import com.example.minishop.data.productsDto
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.repository.category.CategoryRepository
import com.example.minishop.data.repository.products.ProductsRepository
import com.example.minishop.feature.products.home.CategoriesUiState
import com.example.minishop.feature.products.home.HomeScreenUiEvent
import com.example.minishop.feature.products.home.HomeUiState
import com.example.minishop.feature.products.home.HomeViewModel
import com.example.minishop.feature.products.home.ProductsUiState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val categoryRepository: CategoryRepository = mockk()
    private val productsRepository: ProductsRepository = mockk()
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(productsRepository, categoryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Load initial data tests
    @Test
    fun `init emits loading then success for categories and products`() = runTest {
        coEvery {
            categoryRepository.getCategories()
        } coAnswers { NetworkResult.Success(categoriesDto) }

        coEvery {
            productsRepository.getAllProducts()
        } coAnswers { NetworkResult.Success(productsDto) }

        viewModel.uiState.test {
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = true, data = null, error = null),
                    ProductsUiState(isLoading = true, data = null, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(
                        isLoading = false,
                        data = categories,
                        error = null
                    ), ProductsUiState(isLoading = false, data = products, error = null)
                ), awaitItem()
            )
        }
    }

    @Test
    fun `init emits loading then error for categories and products`() = runTest {
        coEvery {
            categoryRepository.getCategories()
        } coAnswers { NetworkResult.Error("Error loading categories") }

        coEvery {
            productsRepository.getAllProducts()
        } coAnswers { NetworkResult.Error("Error loading products") }

        viewModel.uiState.test {
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = true, data = null, error = null),
                    ProductsUiState(isLoading = true, data = null, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(
                        isLoading = false,
                        data = null,
                        error = "Error loading categories"
                    ),
                    ProductsUiState(
                        isLoading = false,
                        data = null,
                        error = "Error loading products"
                    )
                ), awaitItem()
            )
        }
    }

    @Test
    fun `init emits loading then categories error and products success`() = runTest {
        coEvery {
            categoryRepository.getCategories()
        } coAnswers { NetworkResult.Error("Error loading categories") }

        coEvery {
            productsRepository.getAllProducts()
        } coAnswers { NetworkResult.Success(productsDto) }

        viewModel.uiState.test {
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = true, data = null, error = null),
                    ProductsUiState(isLoading = true, data = null, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(
                        isLoading = false,
                        data = null,
                        error = "Error loading categories"
                    ), ProductsUiState(isLoading = false, data = products, error = null)
                ), awaitItem()
            )
        }
    }

    @Test
    fun `init emits loading then categories success and products error`() = runTest {
        coEvery {
            categoryRepository.getCategories()
        } coAnswers { NetworkResult.Success(categoriesDto) }

        coEvery {
            productsRepository.getAllProducts()
        } coAnswers { NetworkResult.Error("Error loading products") }

        viewModel.uiState.test {
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = true, data = null, error = null),
                    ProductsUiState(isLoading = true, data = null, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(
                        isLoading = false,
                        data = categories,
                        error = null
                    ),
                    ProductsUiState(
                        isLoading = false,
                        data = null,
                        error = "Error loading products"
                    )
                ), awaitItem()
            )
        }
    }

    // Category selection tests
    @Test
    fun `selecting category all emits loading then initial data and then nothing changes`() = runTest {
        coEvery {
            categoryRepository.getCategories()
        } coAnswers { NetworkResult.Success(categoriesDto) }

        coEvery {
            productsRepository.getAllProducts()
        } coAnswers { NetworkResult.Success(productsDto) }

        viewModel.uiState.test {
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = true, data = null, error = null),
                    ProductsUiState(isLoading = true, data = null, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = false, data = categories, error = null),
                    ProductsUiState(isLoading = false, data = products, error = null)
                ), awaitItem()
            )
            viewModel.onEvent(HomeScreenUiEvent.OnCategorySelected("all"))
            advanceUntilIdle()
            expectNoEvents()
        }
    }

    @Test
    fun `selecting category electronics emits loading then initial data then selected products`() = runTest {
        coEvery {
            categoryRepository.getCategories()
        } coAnswers { NetworkResult.Success(categoriesDto) }

        coEvery {
            productsRepository.getAllProducts()
        } coAnswers { NetworkResult.Success(productsDto) }

        viewModel.uiState.test {
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = true, data = null, error = null),
                    ProductsUiState(isLoading = true, data = null, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = false, data = categories, error = null),
                    ProductsUiState(isLoading = false, data = products, error = null)
                ), awaitItem()
            )
            viewModel.onEvent(HomeScreenUiEvent.OnCategorySelected("electronics"))
            advanceUntilIdle()
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = false, data = categories, error = null, selectedCategory = "electronics"),
                    ProductsUiState(isLoading = false, data = products, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = false, data = categories, error = null, selectedCategory = "electronics"),
                    ProductsUiState(isLoading = false, data = electronicProducts, error = null)
                ), awaitItem()
            )
        }
    }

    @Test
    fun `selecting category jewelery emits loading then initial data then empty list`() = runTest {
        coEvery {
            categoryRepository.getCategories()
        } coAnswers { NetworkResult.Success(categoriesDto) }

        coEvery {
            productsRepository.getAllProducts()
        } coAnswers { NetworkResult.Success(productsDto) }

        viewModel.uiState.test {
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = true, data = null, error = null),
                    ProductsUiState(isLoading = true, data = null, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = false, data = categories, error = null),
                    ProductsUiState(isLoading = false, data = products, error = null)
                ), awaitItem()
            )
            viewModel.onEvent(HomeScreenUiEvent.OnCategorySelected("jewelery"))
            advanceUntilIdle()
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = false, data = categories, error = null, selectedCategory = "jewelery"),
                    ProductsUiState(isLoading = false, data = products, error = null)
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    CategoriesUiState(isLoading = false, data = categories, error = null, selectedCategory = "jewelery"),
                    ProductsUiState(isLoading = false, data = emptyList(), error = null)
                ), awaitItem()
            )
        }
    }
}