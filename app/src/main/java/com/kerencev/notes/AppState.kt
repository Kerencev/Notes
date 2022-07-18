package com.kerencev.notes

sealed class AppState {
    data class Success<T>(val data: T) : AppState()
    object Loading : AppState()
    object Error : AppState()
}