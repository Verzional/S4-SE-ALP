package com.example.hyrd.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyrd.model.UserModel
import com.example.hyrd.repository.FirebaseAuthRepository
import com.example.hyrd.uiState.AuthUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

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
        val user = firebaseAuthRepository.currentUser
        _uiState.value = AuthUIState(isLoggedIn = user != null, user = user)
        Log.d(TAG, "Current user checked: ${user?.uid}, isLoggedIn: ${user != null}")
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUIState(isLoading = true)
            Log.d(TAG, "Login attempt started for email: $email")
            try {
                val result = firebaseAuthRepository.login(email, password)
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = AuthUIState(isLoggedIn = true, user = user, isLoading = false)
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
                role = selectedRole,
                name = fullName,
                email = email,
                phone_number = phoneNumber,
                date_of_birth = dobDate
            )

            try {
                val result = firebaseAuthRepository.register(email, passwordText, userModel)
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = AuthUIState(isLoggedIn = true, user = user, isLoading = false)
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
        _uiState.value = AuthUIState(isLoggedIn = false, user = null)
        Log.d(TAG, "User signed out.")
    }
}
