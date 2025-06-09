package com.example.hyrd_v2.uiState

data class UpdateApplicationStatusUIState(
    val isLoading: Boolean = false,
    val statusUpdated: Boolean = false,
    val error: String? = null
)