package com.example.hyrd_v2.uiState

data class ApplicationUIState(
    val isLoading: Boolean = false,
    val applicationSubmitted: Boolean = false,
    val hasApplied: Boolean = false,
    val applicationStatus: String? = null,
    val error: String? = null
)
