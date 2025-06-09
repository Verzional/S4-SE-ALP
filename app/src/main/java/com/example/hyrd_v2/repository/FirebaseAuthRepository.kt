package com.example.hyrd_v2.repository

import android.util.Log
import com.example.hyrd_v2.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    companion object {
        private const val TAG = "FirebaseAuthRepository"
    }

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    suspend fun register(email: String, password: String, userModel: UserModel): Result<FirebaseUser> {
        Log.d(TAG, "Attempting to register user with email: $email")
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                Log.i(TAG, "Firebase Auth user created successfully: ${user.uid}")
                val userData = userModel.copy(
                    user_id = user.uid,
                    email = email, // Use the email passed to the function
                    password = "" // Important: Never store plain text passwords
                )
                Log.d(TAG, "Attempting to save user data to Firestore for user: ${user.uid}")
                firestore.collection("users")
                    .document(user.uid)
                    .set(userData)
                    .await() // This is a critical point
                Log.i(TAG, "User data saved to Firestore successfully for user: ${user.uid}")
                Result.success(user)
            } else {
                Log.e(TAG, "Registration failed: Firebase Auth user is null after creation.")
                Result.failure(Exception("Registration failed: Firebase user is null after creation."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during registration for email $email: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        Log.d(TAG, "Attempting to login user with email: $email")
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Log.i(TAG, "Login successful for user: ${user.uid}")
                Result.success(user)
            } else {
                Log.e(TAG, "Login failed: Firebase Auth user is null after sign-in.")
                Result.failure(Exception("Login failed: User is null"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during login for email $email: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<UserModel> {
        val userId = currentUser?.uid
        if (userId == null) {
            Log.w(TAG, "getUserProfile called but no authenticated user found.")
            return Result.failure(Exception("User not authenticated"))
        }
        Log.d(TAG, "Attempting to get user profile for user: $userId")
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val userModel = document.toObject(UserModel::class.java)
            if (userModel != null) {
                Log.i(TAG, "User profile found for user: $userId")
                Result.success(userModel)
            } else {
                Log.w(TAG, "User profile not found in Firestore for user: $userId")
                Result.failure(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user profile for user $userId: ${e.message}", e)
            Result.failure(e)
        }
    }

    fun signOut() {
        Log.d(TAG, "Signing out user: ${currentUser?.uid}")
        firebaseAuth.signOut()
    }
}
