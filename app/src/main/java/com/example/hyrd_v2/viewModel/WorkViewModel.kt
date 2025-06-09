package com.example.hyrd_v2.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hyrd_v2.model.WorkModel
import com.example.hyrd_v2.repository.FirebaseWorkRepository
import com.example.hyrd_v2.repository.FirebaseAuthRepository
import com.example.hyrd_v2.uiState.WorkUIState
import com.example.hyrd_v2.uiState.WorkListUIState
import com.example.hyrd_v2.uiState.WorkDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkViewModel @Inject constructor(
    private val jobRepository: FirebaseWorkRepository,
    private val authRepository: FirebaseAuthRepository // To get current user if needed for employer specific actions
) : ViewModel() {

    private val _workListState = MutableStateFlow(WorkListUIState())
    val workListState: StateFlow<WorkListUIState> = _workListState.asStateFlow()

    private val _workDetailState = MutableStateFlow(WorkDetailUIState())
    val workDetailState: StateFlow<WorkDetailUIState> = _workDetailState.asStateFlow()

    private val _jobCrudState = MutableStateFlow(WorkUIState()) // For create, update, delete
    val jobCrudState: StateFlow<WorkUIState> = _jobCrudState.asStateFlow()

    fun loadAllJobs() {
        viewModelScope.launch {
            _workListState.value = WorkListUIState(isLoading = true)
            try {
                val result = jobRepository.loadAllJobs()
                result.fold(
                    onSuccess = { jobs ->
                        _workListState.value = WorkListUIState(jobs = jobs, isLoading = false)
                    },
                    onFailure = { exception ->
                        _workListState.value = WorkListUIState(error = exception.message, isLoading = false)
                    }
                )
            } catch (e: Exception) {
                _workListState.value = WorkListUIState(error = e.message, isLoading = false)
            }
        }
    }

    fun loadJobDetail(workId: String) {
        viewModelScope.launch {
            _workDetailState.value = WorkDetailUIState(isLoading = true)
            try {
                val result = jobRepository.loadJobDetail(workId)
                result.fold(
                    onSuccess = { job ->
                        _workDetailState.value = WorkDetailUIState(job = job, isLoading = false)
                    },
                    onFailure = { exception ->
                        _workDetailState.value = WorkDetailUIState(error = exception.message, isLoading = false)
                    }
                )
            } catch (e: Exception) {
                _workDetailState.value = WorkDetailUIState(error = e.message, isLoading = false)
            }
        }
    }

    fun createJob(workModel: WorkModel) {
        viewModelScope.launch {
            _jobCrudState.value = WorkUIState(isLoading = true)
            // val currentUser = authRepository.currentUser // If you need to associate job with employer ID
            // val jobWithEmployer = workModel.copy(employerId = currentUser?.uid) // Requires employerId in WorkModel
            try {
                val result = jobRepository.createJob(workModel) // Pass jobWithEmployer if using employerId
                result.fold(
                    onSuccess = {
                        _jobCrudState.value = WorkUIState(jobCreated = true, isLoading = false)
                        loadAllJobs() // Refresh the list
                    },
                    onFailure = { exception ->
                        _jobCrudState.value = WorkUIState(error = exception.message, isLoading = false)
                    }
                )
            } catch (e: Exception) {
                _jobCrudState.value = WorkUIState(error = e.message, isLoading = false)
            }
        }
    }

    fun updateJob(workModel: WorkModel) {
        viewModelScope.launch {
            _jobCrudState.value = WorkUIState(isLoading = true)
            try {
                val result = jobRepository.updateJob(workModel)
                result.fold(
                    onSuccess = {
                        _jobCrudState.value = WorkUIState(jobUpdated = true, isLoading = false)
                        loadAllJobs() // Refresh list
                        if (_workDetailState.value.job?.work_id == workModel.work_id) { // If detail view is showing this job
                            loadJobDetail(workModel.work_id) // Refresh detail
                        }
                    },
                    onFailure = { exception ->
                        _jobCrudState.value = WorkUIState(error = exception.message, isLoading = false)
                    }
                )
            } catch (e: Exception) {
                _jobCrudState.value = WorkUIState(error = e.message, isLoading = false)
            }
        }
    }

    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            _jobCrudState.value = WorkUIState(isLoading = true)
            try {
                val result = jobRepository.deleteJob(jobId)
                result.fold(
                    onSuccess = {
                        _jobCrudState.value = WorkUIState(isLoading = false) // Reset state, maybe add a jobDeleted flag
                        loadAllJobs() // Refresh list
                        if (_workDetailState.value.job?.work_id == jobId) { // If detail view is showing this job
                            _workDetailState.value = WorkDetailUIState(job = null) // Clear detail or navigate back
                        }
                    },
                    onFailure = { exception ->
                        _jobCrudState.value = WorkUIState(error = exception.message, isLoading = false)
                    }
                )
            } catch (e: Exception) {
                _jobCrudState.value = WorkUIState(error = e.message, isLoading = false)
            }
        }
    }

    fun searchJobs(query: String) {
        viewModelScope.launch {
            _workListState.value = WorkListUIState(isLoading = true)
            if (query.isBlank()) {
                loadAllJobs()
                return@launch
            }
            jobRepository.searchJobs(query)
                .catch { exception ->
                    _workListState.value = WorkListUIState(error = exception.message, isLoading = false)
                }
                .collect { jobs ->
                    _workListState.value = WorkListUIState(jobs = jobs, isLoading = false)
                }
        }
    }

    fun resetJobCrudState() {
        _jobCrudState.value = WorkUIState()
    }
}