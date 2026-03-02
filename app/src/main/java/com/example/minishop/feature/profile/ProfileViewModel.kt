package com.example.minishop.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minishop.data.repository.authorization.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun onEvent(event: ProfileScreenUiEvent) {
        when(event) {
            is ProfileScreenUiEvent.LogOut -> logOut()
        }
    }
    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut()
        }
    }
}