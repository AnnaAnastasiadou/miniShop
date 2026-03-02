package com.example.minishop.feature.profile

sealed class ProfileScreenUiEvent {
    data object LogOut: ProfileScreenUiEvent()
}
