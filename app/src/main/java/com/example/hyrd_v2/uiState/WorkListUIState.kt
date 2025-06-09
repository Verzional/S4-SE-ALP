package com.example.hyrd_v2.uiState

import com.example.hyrd_v2.model.WorkModel

data class WorkListUIState(
    val isLoading: Boolean = false,
    val jobs: List<WorkModel> = emptyList(),
    val error: String? = null
)
