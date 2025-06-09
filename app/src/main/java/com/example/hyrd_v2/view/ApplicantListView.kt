package com.example.hyrd_v2.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hyrd_v2.viewModel.ApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantListView(
    navController: NavController, workId: String, // Passed from navigation
    applicationViewModel: ApplicationViewModel = hiltViewModel()
) {
    val applicantListState by applicationViewModel.applicantListState.collectAsState()
    val updateStatusState by applicationViewModel.updateStatusState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(workId) {
        applicationViewModel.loadApplicantsForJob(workId)
    }

    LaunchedEffect(updateStatusState) {
        if (updateStatusState.statusUpdated) {
            Toast.makeText(context, "Applicant status updated!", Toast.LENGTH_SHORT).show()
            applicationViewModel.resetUpdateStatusState() // Reset state
            // List is already refreshed by the ViewModel
        }
        updateStatusState.error?.let {
            Toast.makeText(context, "Error updating status: $it", Toast.LENGTH_LONG).show()
            applicationViewModel.resetUpdateStatusState() // Reset state
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Applicants", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp) // Adjusted padding
        ) {
            when {
                applicantListState.isLoading || updateStatusState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                        if (updateStatusState.isLoading) {
                            Text("Updating status...", modifier = Modifier.padding(top = 60.dp))
                        }
                    }
                }

                applicantListState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Error: ${applicantListState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                applicantListState.applicants.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No applicants for this job yet.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Increased spacing
                    ) {
                        items(
                            applicantListState.applicants,
                            key = { it.application_id }) { application ->
                            ApplicantCardView(
                                application = application, onStatusChange = { newStatus ->
                                    applicationViewModel.updateApplicationStatus(
                                        application.application_id,
                                        newStatus,
                                        workId // Pass workId to refresh the correct list
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}