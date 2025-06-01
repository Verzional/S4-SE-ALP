package com.example.hyrd.uiState

data class WorkUIState(
    val isLoading: Boolean = false,
    val jobCreated: Boolean = false,
    val jobUpdated: Boolean = false,
    val error: String? = null
)