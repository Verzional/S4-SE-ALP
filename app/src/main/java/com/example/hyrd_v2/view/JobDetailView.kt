package com.example.hyrd_v2.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hyrd_v2.R
import com.example.hyrd_v2.viewModel.ApplicationViewModel
import com.example.hyrd_v2.viewModel.AuthViewModel
import com.example.hyrd_v2.viewModel.WorkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailView(
    workId: String,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(), // To get user role and profile
    workViewModel: WorkViewModel = hiltViewModel(),
    applicationViewModel: ApplicationViewModel = hiltViewModel()
) {
    val workDetailState by workViewModel.workDetailState.collectAsState()
    val applyJobState by applicationViewModel.applyJobState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(workId) {
        workViewModel.loadJobDetail(workId)
        if (authState.user != null) { // Check if user has already applied
            applicationViewModel.checkIfAlreadyApplied(workId)
        }
    }

    LaunchedEffect(applyJobState) {
        if (applyJobState.applicationSubmitted) {
            Toast.makeText(context, "Application submitted successfully!", Toast.LENGTH_LONG).show()
            applicationViewModel.resetApplyJobState() // Reset state
            navController.navigate("successfulApply") {
                // Optional: popUpTo(navController.graph.findStartDestination().id) or specific route
            }
        }
        applyJobState.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            applicationViewModel.resetApplyJobState() // Reset state
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        workDetailState.job?.name ?: "Loading...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }) { innerPadding ->
        when {
            workDetailState.isLoading -> { /* ... Loading UI ... */
            }

            workDetailState.error != null -> { /* ... Error UI ... */
            }

            workDetailState.job != null -> {
                val job = workDetailState.job!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // ... (Job details: description, wage, location, etc. - already implemented)
                    Text(
                        text = job.description,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    JobDetailItem("Wage:", "IDR ${"%,.0f".format(job.wage)}")
                    JobDetailItem("Location:", job.location)
                    JobDetailItem("Work Hours:", job.work_hour)
                    JobDetailItem("Job Type/Category:", job.job_type)
                    JobDetailItem("Quota:", "${job.quota} workers")


                    Spacer(modifier = Modifier.height(24.dp))

                    // CV Upload Section (Remains a placeholder visually)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(
                                    context, "CV Upload feature coming soon!", Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0FBE97)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_arrow_upward_alt_24),
                                contentDescription = "Upload",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Upload CV", color = Color.White)
                        }
                        Column {
                            Text(
                                text = "Formats accepted: .pdf, .doc, .docx",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "*required for application",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Pushes buttons to bottom

                    // "View Applicants" Button for Employers
                    if (authState.userProfile?.role == "Employer") {
                        Button(
                            onClick = {
                                navController.navigate("applicants/${job.work_id}")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(bottom = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "View Applicants Icon",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "View Applicants (${workDetailState.job?.quota ?: 0} needed)", // Show quota
                                color = MaterialTheme.colorScheme.onSecondary, fontSize = 16.sp
                            )
                        }
                    }

                    // Apply Job Button for Employees
                    if (authState.userProfile?.role == "Employee") {
                        when {
                            applyJobState.isLoading -> CircularProgressIndicator(
                                modifier = Modifier.align(
                                    Alignment.CenterHorizontally
                                )
                            )

                            applyJobState.hasApplied -> {
                                Text(
                                    "You have already applied for this job. Status: ${applyJobState.existingApplicationStatus}",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier
                                        .padding(vertical = 16.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            else -> {
                                Button(
                                    onClick = {
                                        // For now, use a dummy CV path.
                                        // In a real app, you'd get this from an upload process.
                                        val dummyCvPath = "cv_uploads/user_cv.pdf"
                                        if (authState.userProfile != null) {
                                            applicationViewModel.applyForJob(
                                                job.work_id, dummyCvPath, authState.userProfile
                                            )
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "User profile not loaded. Cannot apply.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Apply for this Job",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> { /* ... No job found UI ... */
            }
        }
    }
}

// JobDetailItem composable remains the same
// @Composable
// fun JobDetailItem(label: String, value: String) { ... }

@Composable
fun JobDetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
    }
}