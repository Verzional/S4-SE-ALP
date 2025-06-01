package com.example.hyrd.repository

import com.example.hyrd.model.UserModel
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

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    suspend fun register(email: String, password: String, userModel: UserModel): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val userData = userModel.copy(user_id = user.uid)
                firestore.collection("users")
                    .document(user.uid)
                    .set(userData)
                    .await()
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<UserModel> {
        return try {
            val userId = currentUser?.uid ?: throw Exception("User not authenticated")
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val userModel = document.toObject(UserModel::class.java)
            if (userModel != null) {
                Result.success(userModel)
            } else {
                Result.failure(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}