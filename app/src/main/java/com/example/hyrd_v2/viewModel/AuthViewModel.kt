package com.example.hyrd_v2.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyrd_v2.model.UserModel
import com.example.hyrd_v2.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.example.hyrd_v2.uiState.AuthUIState

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUIState())
    val uiState: StateFlow<AuthUIState> = _uiState.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    companion object {
        private const val TAG = "AuthViewModel"
    }

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            val user = firebaseAuthRepository.currentUser
            if (user != null) {
                _uiState.value = AuthUIState(isLoggedIn = true, user = user)
                fetchUserProfile() // Fetch profile if user is logged in
            } else {
                _uiState.value = AuthUIState(isLoggedIn = false, user = null, userProfile = null)
            }
            Log.d(TAG, "Current user checked: ${user?.uid}, isLoggedIn: ${user != null}")
        }
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            if (_uiState.value.user == null) return@launch // No user to fetch profile for

            // Optimistically keep isLoading from previous state or set it if critical path
            // _uiState.value = _uiState.value.copy(isLoading = true) // Optional: show loading for profile fetch

            val profileResult = firebaseAuthRepository.getUserProfile()
            profileResult.fold(
                onSuccess = { userModel ->
                    _uiState.value = _uiState.value.copy(userProfile = userModel, isLoading = false)
                    Log.i(TAG, "User profile fetched successfully for role: ${userModel.role}")
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(error = "Failed to fetch profile: ${exception.message}", isLoading = false)
                    Log.e(TAG, "Failed to fetch user profile: ${exception.message}")
                }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUIState(isLoading = true)
            Log.d(TAG, "Login attempt started for email: $email")
            try {
                val result = firebaseAuthRepository.login(email, password)
                result.fold(
                    onSuccess = { user ->
                        // User is set, now fetch profile
                        _uiState.value = AuthUIState(isLoggedIn = true, user = user, isLoading = true) // isLoading true until profile is fetched
                        fetchUserProfile() // This will update isLoading to false once done/failed
                        Log.i(TAG, "Login successful for user: ${user.uid}")
                    },
                    onFailure = { exception ->
                        _uiState.value = AuthUIState(isLoading = false, error = exception.message)
                        Log.e(TAG, "Login failed: ${exception.message}", exception)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = AuthUIState(isLoading = false, error = "An unexpected error occurred during login.")
                Log.e(TAG, "Unexpected error during login: ${e.message}", e)
            }
        }
    }

    fun registerUser(
        fullName: String,
        phoneNumber: String,
        dateOfBirthString: String,
        email: String,
        passwordText: String,
        selectedRole: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUIState(isLoading = true, error = null)
            _registrationSuccess.value = false
            Log.d(TAG, "Registration attempt started for email: $email")

            val dobDate: Date? = try {
                if (dateOfBirthString.isNotBlank()) {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateOfBirthString)
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Invalid date format: $dateOfBirthString", e)
                _uiState.value = AuthUIState(isLoading = false, error = "Invalid Date of Birth format. Use dd/MM/yyyy or leave blank.")
                return@launch
            }

            val userModel = UserModel(
                // user_id will be set by repository after auth creation
                role = selectedRole,
                name = fullName,
                email = email, // Will be overwritten by repo if needed, but good to pass
                password = "", // NEVER store plain password here
                phone_number = phoneNumber,
                date_of_birth = dobDate,
                // bio = "", // Initialize other fields if necessary
                // profile_picture = "",
                // bio_image = ""
            )

            try {
                val result = firebaseAuthRepository.register(email, passwordText, userModel)
                result.fold(
                    onSuccess = { user ->
                        // User is set, now fetch profile (though for registration, userModel is already partially known)
                        _uiState.value = AuthUIState(isLoggedIn = true, user = user, isLoading = true) // isLoading true until profile is "confirmed"
                        // For new registration, the passed userModel can be used directly for userProfile
                        // or re-fetch to confirm it's saved correctly.
                        // For simplicity, let's assume the register function in repo saves it, and we can use the passed one.
                        _uiState.value = _uiState.value.copy(userProfile = userModel.copy(user_id = user.uid, email = user.email ?: email), isLoading = false)

                        _registrationSuccess.value = true
                        Log.i(TAG, "Registration successful for user: ${user.uid}")
                    },
                    onFailure = { exception ->
                        _uiState.value = AuthUIState(isLoading = false, error = exception.message)
                        Log.e(TAG, "Registration failed: ${exception.message}", exception)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = AuthUIState(isLoading = false, error = "An unexpected error occurred during registration.")
                Log.e(TAG, "Unexpected error during registration: ${e.message}", e)
            }
        }
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }

    fun signOut() {
        firebaseAuthRepository.signOut()
        _uiState.value = AuthUIState(isLoggedIn = false, user = null, userProfile = null) // Clear profile on sign out
        Log.d(TAG, "User signed out.")
    }
}