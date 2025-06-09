package com.example.hyrd_v2.repository

import android.util.Log
import com.example.hyrd_v2.model.WorkModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseWorkRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "FirebaseJobRepository"
        private const val JOBS_COLLECTION = "jobs" // Changed from "works" to "jobs" based on createJob logic
    }

    suspend fun createJob(workModel: WorkModel): Result<String> {
        return try {
            // If work_id is not pre-set, Firestore generates one.
            // If you want to set it yourself before calling, ensure it's unique.
            val docRef = if (workModel.work_id.isBlank()) {
                firestore.collection(JOBS_COLLECTION).document()
            } else {
                firestore.collection(JOBS_COLLECTION).document(workModel.work_id)
            }
            val jobWithId = workModel.copy(work_id = docRef.id)
            docRef.set(jobWithId).await()
            Log.d(TAG, "Job created with ID: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating job", e)
            Result.failure(e)
        }
    }

    suspend fun loadAllJobs(): Result<List<WorkModel>> {
        return try {
            val snapshot = firestore.collection(JOBS_COLLECTION)
                .whereEqualTo("status", true) // Assuming status true means active
                .orderBy("name") // Optional: order by name or creation date
                .get()
                .await()

            val jobs = snapshot.documents.mapNotNull { doc ->
                doc.toObject(WorkModel::class.java)
            }
            Log.d(TAG, "Loaded ${jobs.size} jobs.")
            Result.success(jobs)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading all jobs", e)
            Result.failure(e)
        }
    }

    suspend fun loadJobDetail(workId: String): Result<WorkModel> {
        return try {
            val document = firestore.collection(JOBS_COLLECTION)
                .document(workId)
                .get()
                .await()

            val job = document.toObject(WorkModel::class.java)
            if (job != null) {
                Log.d(TAG, "Job detail loaded for ID: $workId")
                Result.success(job)
            } else {
                Log.w(TAG, "Job not found for ID: $workId")
                Result.failure(Exception("Job not found"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading job detail for ID: $workId", e)
            Result.failure(e)
        }
    }

    suspend fun updateJob(workData: WorkModel): Result<Unit> {
        return try {
            if (workData.work_id.isBlank()) {
                return Result.failure(IllegalArgumentException("Work ID cannot be blank for update"))
            }
            firestore.collection(JOBS_COLLECTION)
                .document(workData.work_id)
                .set(workData) // set will overwrite, use update for specific fields
                .await()
            Log.d(TAG, "Job updated for ID: ${workData.work_id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating job for ID: ${workData.work_id}", e)
            Result.failure(e)
        }
    }

    suspend fun deleteJob(jobId: String): Result<Unit> {
        return try {
            if (jobId.isBlank()) {
                return Result.failure(IllegalArgumentException("Job ID cannot be blank for delete"))
            }
            firestore.collection(JOBS_COLLECTION)
                .document(jobId)
                .delete()
                .await()
            Log.d(TAG, "Job deleted for ID: $jobId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting job for ID: $jobId", e)
            Result.failure(e)
        }
    }

    fun searchJobs(queryText: String): Flow<List<WorkModel>> = callbackFlow {
        Log.d(TAG, "Searching jobs with query: $queryText")
        val query = firestore.collection(JOBS_COLLECTION)
            .whereEqualTo("status", true)
            // Basic name search (case-sensitive).
            // For case-insensitive, you'd typically store a lowercased version of the name.
            // Firestore doesn't support case-insensitive queries directly on mixed-case fields.
            // This query will find names starting with queryText.
            .orderBy("name")
            .startAt(queryText)
            .endAt(queryText + '\uf8ff') // '\uf8ff' is a common trick for "prefix" searches

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error in searchJobs listener", error)
                close(error) // Close the flow with an error
                return@addSnapshotListener
            }

            val jobs = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(WorkModel::class.java)
            } ?: emptyList()
            Log.d(TAG, "Search returned ${jobs.size} jobs for query: $queryText")
            trySend(jobs).isSuccess // Offer the result to the flow
        }

        awaitClose {
            Log.d(TAG, "Closing searchJobs listener for query: $queryText")
            listener.remove() // Clean up the listener when the flow is closed
        }
    }
}