package com.example.hyrd_v2.uiState

data class WorkUIState(
    val isLoading: Boolean = false,
    val jobCreated: Boolean = false,
    val jobUpdated: Boolean = false,
    val error: String? = null
)