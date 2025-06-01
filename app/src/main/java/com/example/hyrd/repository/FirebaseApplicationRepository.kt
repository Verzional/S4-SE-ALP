package com.example.hyrd.repository

import com.example.hyrd.model.ApplicationModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseApplicationRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun applyForJob(applicationModel: ApplicationModel): Result<String> {
        return try {
            val docRef = firestore.collection("applications").document()
            val applicationWithId = applicationModel.copy(
                worker_id = docRef.id,
                application_date = java.util.Date().toString()
            )
            docRef.set(applicationWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkApplicationStatus(workerId: String, workId: String): Result<ApplicationModel?> {
        return try {
            val snapshot = firestore.collection("applications")
                .whereEqualTo("worker_id", workerId)
                .whereEqualTo("work_id", workId)
                .get()
                .await()

            val application = snapshot.documents.firstOrNull()?.toObject(ApplicationModel::class.java)
            Result.success(application)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadApplicants(workId: String): Result<List<ApplicationModel>> {
        return try {
            val snapshot = firestore.collection("applications")
                .whereEqualTo("work_id", workId)
                .get()
                .await()

            val applications = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ApplicationModel::class.java)
            }
            Result.success(applications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadMyApplications(workerId: String): Result<List<ApplicationModel>> {
        return try {
            val snapshot = firestore.collection("applications")
                .whereEqualTo("worker_id", workerId)
                .get()
                .await()

            val applications = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ApplicationModel::class.java)
            }
            Result.success(applications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptApplication(workerId: String, workId: String): Result<Unit> {
        return try {
            val snapshot = firestore.collection("applications")
                .whereEqualTo("worker_id", workerId)
                .whereEqualTo("work_id", workId)
                .get()
                .await()

            val document = snapshot.documents.firstOrNull()
            if (document != null) {
                firestore.collection("applications")
                    .document(document.id)
                    .update("status", "accepted")
                    .await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Application not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectApplication(workerId: String, workId: String): Result<Unit> {
        return try {
            val snapshot = firestore.collection("applications")
                .whereEqualTo("worker_id", workerId)
                .whereEqualTo("work_id", workId)
                .get()
                .await()

            val document = snapshot.documents.firstOrNull()
            if (document != null) {
                firestore.collection("applications")
                    .document(document.id)
                    .update("status", "rejected")
                    .await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Application not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}