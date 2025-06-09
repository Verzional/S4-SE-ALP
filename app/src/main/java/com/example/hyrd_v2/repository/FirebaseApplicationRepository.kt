package com.example.hyrd_v2.repository

import android.util.Log
import com.example.hyrd_v2.model.ApplicationModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseApplicationRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "FirebaseApplicationRepo"
        private const val APPLICATIONS_COLLECTION = "applications"
    }

    suspend fun applyForJob(applicationModel: ApplicationModel): Result<String> {
        return try {
            val docRef = firestore.collection(APPLICATIONS_COLLECTION).document()
            // Ensure application_date is set by server or client before saving if not using @ServerTimestamp directly for creation
            // For @ServerTimestamp to work on creation, you might need to pass null and let Firestore fill it.
            // Or use FieldValue.serverTimestamp() if setting manually in a map.
            // Here, we assume @ServerTimestamp on the model handles it.
            val applicationWithId = applicationModel.copy(
                application_id = docRef.id
                // application_date will be handled by @ServerTimestamp
            )
            docRef.set(applicationWithId).await()
            Log.d(TAG, "Application submitted with ID: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error applying for job", e)
            Result.failure(e)
        }
    }

    suspend fun checkApplicationStatus(
        workerId: String,
        workId: String
    ): Result<ApplicationModel?> {
        return try {
            val snapshot = firestore.collection(APPLICATIONS_COLLECTION)
                .whereEqualTo("worker_id", workerId)
                .whereEqualTo("work_id", workId)
                .limit(1) // Expect at most one application
                .get()
                .await()

            val application =
                snapshot.documents.firstOrNull()?.toObject(ApplicationModel::class.java)
            if (application != null) {
                Log.d(
                    TAG,
                    "Application status checked for worker $workerId on job $workId. Status: ${application.status}"
                )
            } else {
                Log.d(TAG, "No application found for worker $workerId on job $workId.")
            }
            Result.success(application)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking application status for worker $workerId on job $workId", e)
            Result.failure(e)
        }
    }

    suspend fun loadApplicants(workId: String): Result<List<ApplicationModel>> {
        return try {
            val snapshot = firestore.collection(APPLICATIONS_COLLECTION)
                .whereEqualTo("work_id", workId)
                // .orderBy("application_date", Query.Direction.DESCENDING) // Optional: order by date
                .get()
                .await()

            val applications = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ApplicationModel::class.java)
            }
            Log.d(TAG, "Loaded ${applications.size} applicants for job $workId")
            Result.success(applications)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading applicants for job $workId", e)
            Result.failure(e)
        }
    }

    suspend fun loadMyApplications(workerId: String): Result<List<ApplicationModel>> {
        return try {
            val snapshot = firestore.collection(APPLICATIONS_COLLECTION)
                .whereEqualTo("worker_id", workerId)
                // .orderBy("application_date", Query.Direction.DESCENDING) // Optional
                .get()
                .await()

            val applications = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ApplicationModel::class.java)
            }
            Log.d(TAG, "Loaded ${applications.size} applications for worker $workerId")
            Result.success(applications)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading applications for worker $workerId", e)
            Result.failure(e)
        }
    }

    // Updated to use application_id and newStatus string
    suspend fun updateApplicationStatus(applicationId: String, newStatus: String): Result<Unit> {
        return try {
            if (applicationId.isBlank()) {
                return Result.failure(IllegalArgumentException("Application ID cannot be blank for update."))
            }
            firestore.collection(APPLICATIONS_COLLECTION)
                .document(applicationId)
                .update("status", newStatus) // Only update the status field
                .await()
            Log.d(TAG, "Application status updated for ID $applicationId to $newStatus")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating application status for ID $applicationId", e)
            Result.failure(e)
        }
    }

    // The old accept/reject methods can be removed if updateApplicationStatus replaces them.
    // suspend fun acceptApplication(workerId: String, workId: String): Result<Unit> { ... }
    // suspend fun rejectApplication(workerId: String, workId: String): Result<Unit> { ... }
}