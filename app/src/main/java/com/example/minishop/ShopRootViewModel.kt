package com.example.minishop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.repository.authorization.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopRootViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    val uiState: StateFlow<ShopRootUiState> =
        authRepository.isLoggedIn
            .map { loggedIn -> ShopRootUiState(isLoggedIn = loggedIn) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                initialValue = ShopRootUiState(authRepository.isLoggedIn.value)
            )
}