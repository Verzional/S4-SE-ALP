package com.example.hyrd.uiState

import com.example.hyrd.model.WorkModel

data class WorkListUIState(
    val isLoading: Boolean = false,
    val jobs: List<WorkModel> = emptyList(),
    val error: String? = null
)
