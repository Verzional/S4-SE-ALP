package com.example.hyrd_v2.uiState

import com.example.hyrd_v2.model.ApplicationModel

data class ApplicantListUIState(
    val isLoading: Boolean = false,
    val applicants: List<ApplicationModel> = emptyList(),
    val error: String? = null
)