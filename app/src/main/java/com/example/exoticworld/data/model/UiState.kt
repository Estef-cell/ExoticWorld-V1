package com.example.exoticworld.data.model

/**
 * Estados de UI genéricos para manejo de carga, éxito y error
 */
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
