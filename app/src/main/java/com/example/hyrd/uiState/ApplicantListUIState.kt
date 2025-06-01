package com.example.hyrd.uiState

import com.example.hyrd.model.ApplicationModel

data class ApplicantListUIState(
    val isLoading: Boolean = false,
    val applicants: List<ApplicationModel> = emptyList(),
    val error: String? = null
)