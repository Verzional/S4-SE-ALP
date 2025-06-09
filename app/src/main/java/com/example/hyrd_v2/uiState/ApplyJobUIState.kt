package com.example.hyrd_v2.uiState

data class ApplyJobUIState(
    val isLoading: Boolean = false,
    val applicationSubmitted: Boolean = false,
    val error: String? = null,
    val hasApplied: Boolean = false,
    val existingApplicationStatus: String? = null
)
