package com.example.hyrd_v2.uiState

import com.example.hyrd_v2.model.UserModel

data class AuthUIState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: com.google.firebase.auth.FirebaseUser? = null,
    val userProfile: UserModel? = null, // Added userProfile
    val error: String? = null
)