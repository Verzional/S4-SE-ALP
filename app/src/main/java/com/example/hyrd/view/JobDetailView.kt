package com.example.hyrd.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.hyrd.R
import com.example.hyrd.viewmodel.WorkViewModel

// import com.example.hyrd.viewmodel.ApplicationViewModel // For later apply job integration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailView(
    workId: String, // Passed as navigation argument
    navController: NavController,
    workViewModel: WorkViewModel = hiltViewModel()
    // applicationViewModel: ApplicationViewModel = hiltViewModel() // For later
) {
    LaunchedEffect(workId) {
        workViewModel.loadJobDetail(workId)
    }

    val workDetailState by workViewModel.workDetailState.collectAsState()
    val context = LocalContext.current

    // TODO: Observe applicationViewModel state for apply job status

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = workDetailState.job?.name ?: "Loading...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
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
        }
    ) { innerPadding ->
        when {
            workDetailState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            workDetailState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${workDetailState.error}", color = MaterialTheme.colorScheme.error)
                }
            }

            workDetailState.job != null -> {
                val job = workDetailState.job!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
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
                    // TODO: Add employer information if available and relevant

                    Spacer(modifier = Modifier.height(24.dp))

                    // CV Upload Section (Placeholder for now, actual upload needs more logic)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "CV Upload feature coming soon!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // TODO: Implement actual CV upload logic
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0FBE97)), // Keep custom green
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_arrow_upward_alt_24), // Ensure this drawable exists
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

                    Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

                    Button(
                        onClick = {
                            // TODO: Integrate with ApplicationViewModel.applyForJob(job.work_id, cvPath)
                            Toast.makeText(
                                context,
                                "Apply Job feature coming soon!",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Example: navController.navigate("successfulApply") or handle via ViewModel state
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
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Job not found or no longer available.", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

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
            text = value,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
    }
}