package com.example.hyrd.uiState

data class UpdateApplicationStatusUIState(
    val isLoading: Boolean = false,
    val statusUpdated: Boolean = false,
    val error: String? = null
)