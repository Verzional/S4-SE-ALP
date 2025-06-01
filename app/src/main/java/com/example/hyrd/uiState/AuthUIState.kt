package com.example.hyrd.uiState

data class AuthUIState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: com.google.firebase.auth.FirebaseUser? = null,
    val error: String? = null
)