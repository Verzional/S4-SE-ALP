package com.example.hyrd_v2.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hyrd_v2.model.WorkModel
import com.example.hyrd_v2.viewModel.WorkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobView(
    navController: NavController,
    workViewModel: WorkViewModel = hiltViewModel()
) {
    var jobName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var wage by remember { mutableStateOf("") } // String for input, convert to Double
    var location by remember { mutableStateOf("") }
    var workHours by remember { mutableStateOf("") } // e.g., "Flexible", "9 AM - 5 PM"
    var jobType by remember { mutableStateOf("") } // Single string for job category/type
    var quota by remember { mutableStateOf("") } // String for input, convert to Int

    val context = LocalContext.current
    val jobCrudState by workViewModel.jobCrudState.collectAsState()

    LaunchedEffect(jobCrudState) {
        if (jobCrudState.jobCreated) {
            Toast.makeText(context, "Job created successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack() // Go back to the previous screen
            workViewModel.resetJobCrudState() // Reset the state
        }
        jobCrudState.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            workViewModel.resetJobCrudState() // Reset the state
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Job", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = jobName,
                onValueChange = { jobName = it },
                label = { Text("Job Name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = jobType,
                onValueChange = { jobType = it },
                label = { Text("Job Type/Category *") }, // e.g., "Data Entry", "Writing", "Design"
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = wage,
                onValueChange = { wage = it },
                label = { Text("Wage (IDR) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location *") }, // e.g., "Remote", "Jakarta"
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = workHours,
                onValueChange = { workHours = it },
                label = { Text("Work Hours *") }, // e.g., "Flexible", "Part-time (20hrs/week)"
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = quota,
                onValueChange = { quota = it },
                label = { Text("Quota (Number of workers needed) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (jobCrudState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        val wageDouble = wage.toDoubleOrNull()
                        val quotaInt = quota.toIntOrNull()

                        if (jobName.isBlank() || description.isBlank() || jobType.isBlank() ||
                            location.isBlank() || workHours.isBlank() || wageDouble == null || quotaInt == null
                        ) {
                            Toast.makeText(
                                context,
                                "Please fill all required (*) fields correctly.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }
                        if (wageDouble <= 0 || quotaInt <= 0) {
                            Toast.makeText(
                                context,
                                "Wage and Quota must be positive numbers.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }


                        val newWork = WorkModel(
                            // work_id will be generated by Firestore or repository
                            name = jobName,
                            description = description,
                            job_type = jobType,
                            quota = quotaInt,
                            location = location,
                            work_hour = workHours,
                            wage = wageDouble,
                            status = true // Default to active
                        )
                        workViewModel.createJob(newWork)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Create Job",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}