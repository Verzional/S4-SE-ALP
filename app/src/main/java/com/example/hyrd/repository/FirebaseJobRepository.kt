package com.example.hyrd.repository

import com.example.hyrd.model.WorkModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseJobRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createJob(workModel: WorkModel): Result<String> {
        return try {
            val docRef = firestore.collection("jobs").document()
            val jobWithId = workModel.copy(work_id = docRef.id)
            docRef.set(jobWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadAllJobs(): Result<List<WorkModel>> {
        return try {
            val snapshot = firestore.collection("jobs")
                .whereEqualTo("status", true)
                .get()
                .await()

            val jobs = snapshot.documents.mapNotNull { doc ->
                doc.toObject(WorkModel::class.java)
            }
            Result.success(jobs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadJobDetail(workId: String): Result<WorkModel> {
        return try {
            val document = firestore.collection("jobs")
                .document(workId)
                .get()
                .await()

            val job = document.toObject(WorkModel::class.java)
            if (job != null) {
                Result.success(job)
            } else {
                Result.failure(Exception("Job not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateJob(workData: WorkModel): Result<Unit> {
        return try {
            firestore.collection("jobs")
                .document(workData.work_id)
                .set(workData)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteJob(jobId: String): Result<Unit> {
        return try {
            firestore.collection("jobs")
                .document(jobId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun searchJobs(query: String): Flow<List<WorkModel>> = callbackFlow {
        val listener = firestore.collection("jobs")
            .whereEqualTo("status", true)
            .orderBy("name")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val jobs = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(WorkModel::class.java)
                } ?: emptyList()

                trySend(jobs)
            }

        awaitClose { listener.remove() }
    }
}