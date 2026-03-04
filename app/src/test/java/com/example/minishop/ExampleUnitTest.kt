package com.example.minishop

import app.cash.turbine.test
import com.example.minishop.data.remote.NetworkResult
import com.example.minishop.data.remote.authorization.LoginResponseDto
import com.example.minishop.data.repository.authorization.AuthRepository
import com.example.minishop.feature.login.LogInUiState
import com.example.minishop.feature.login.LogInViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogInViewModelTest {
    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: LogInViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LogInViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is default LogInUiState`() = runTest {
        viewModel.uiState.test {
            assertEquals(LogInUiState(isLoading = false, error = null), awaitItem())
        }
    }

    @Test
    fun `logIn success emits loading then success state`() = runTest {
        coEvery {
            authRepository.logIn("user", "pass")
        } returns NetworkResult.Success(LoginResponseDto(token = "fake-token"))

        viewModel.uiState.test {
            assertEquals(LogInUiState(isLoading = false, error = null), awaitItem())

            viewModel.logIn("user", "pass")
            advanceUntilIdle()

            // Intermediate loading state
            assertEquals(LogInUiState(isLoading = true, error = null), awaitItem())

            // Final success state — back to idle
            assertEquals(LogInUiState(isLoading = false, error = null), awaitItem())
        }
    }

    @Test
    fun `logIn failure emits loading then error state`() = runTest {
        coEvery {
            authRepository.logIn("user", "fail")
        } returns NetworkResult.Error("Error while logging in")

        viewModel.uiState.test {
            assertEquals(LogInUiState(isLoading = false, error = null), awaitItem())

            viewModel.logIn("user", "fail")
            advanceUntilIdle()

            assertEquals(LogInUiState(isLoading = true, error = null), awaitItem())

            assertEquals(LogInUiState(isLoading = false, error = "Error while logging in"), awaitItem())
        }
    }

    @Test
    fun `logIn failure then clearError resets error to null`() = runTest {
        coEvery {
            authRepository.logIn("user", "fail")
        } returns NetworkResult.Error("Error while logging in")

        viewModel.uiState.test {
            awaitItem()

            viewModel.logIn("user", "fail")
            advanceUntilIdle()

            awaitItem()
            assertEquals(LogInUiState(isLoading = false, error = "Error while logging in"), awaitItem())

            viewModel.clearError()
            advanceUntilIdle()

            assertEquals(LogInUiState(isLoading = false, error = null), awaitItem())
        }
    }

    @Test
    fun `clearError on initial state does not emit new value`() = runTest {
        viewModel.uiState.test {
            assertEquals(LogInUiState(isLoading = false, error = null), awaitItem())
            viewModel.clearError()
            expectNoEvents()
        }
    }

    @Test
    fun `logIn success after previous failure clears error`() = runTest {
        coEvery {
            authRepository.logIn("user", "pass")
        } returns NetworkResult.Success(LoginResponseDto(token = "fake-token"))

        coEvery {
            authRepository.logIn("user", "fail")
        } returns NetworkResult.Error("Logging in error")

        viewModel.uiState.test {
            awaitItem()

            viewModel.logIn("user", "fail")

            awaitItem()
            assertEquals(LogInUiState(isLoading = false, error = "Logging in error"), awaitItem())

            viewModel.logIn("user", "pass")

            awaitItem()
            assertEquals(LogInUiState(isLoading = false, error = null), awaitItem())
        }
    }

    @Test
    fun `multiple consecutive failures emit correct error each time`() = runTest {
        coEvery {
            authRepository.logIn("user", "fail")
        } returns NetworkResult.Error("Logging in error")

        viewModel.uiState.test {
            awaitItem()

            viewModel.logIn("user", "fail")

            awaitItem()
            assertEquals(LogInUiState(isLoading = false, error = "Logging in error"), awaitItem())

            viewModel.logIn("user", "fail")

            awaitItem()
            assertEquals(LogInUiState(isLoading = false, error = "Logging in error"), awaitItem())

            viewModel.logIn("user", "fail")

            awaitItem()
            assertEquals(LogInUiState(isLoading = false, error = "Logging in error"), awaitItem())
        }

    }
}
