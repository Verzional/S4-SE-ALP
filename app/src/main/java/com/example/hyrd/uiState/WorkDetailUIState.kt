package com.example.hyrd.uiState

import com.example.hyrd.model.WorkModel

data class WorkDetailUIState(
    val isLoading: Boolean = false,
    val job: WorkModel? = null,
    val error: String? = null
)
