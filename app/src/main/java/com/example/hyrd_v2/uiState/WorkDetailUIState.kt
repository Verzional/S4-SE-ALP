package com.example.hyrd_v2.uiState

import com.example.hyrd_v2.model.WorkModel

data class WorkDetailUIState(
    val isLoading: Boolean = false,
    val job: WorkModel? = null,
    val error: String? = null
)
