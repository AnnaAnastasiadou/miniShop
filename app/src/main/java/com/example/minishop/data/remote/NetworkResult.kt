package com.example.minishop.data.remote

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T): NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing> ()
}