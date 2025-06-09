package com.example.hyrd_v2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hyrd_v2.model.UserModel
import com.example.hyrd_v2.viewModel.AuthViewModel
import com.example.hyrd_v2.viewModel.WorkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListView(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    workViewModel: WorkViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val workListState by workViewModel.workListState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    var currentUserProfile by remember { mutableStateOf<UserModel?>(null) } // To store fetched user profile


    LaunchedEffect(Unit) { // Load jobs on initial composition
        workViewModel.loadAllJobs()
    }

    LaunchedEffect(authState.user) { // Fetch user profile when authState.user changes
        if (authState.user != null && authState.isLoggedIn) {
            authViewModel.fetchUserProfile() // Make sure this function exists in AuthViewModel
        } else {
            currentUserProfile = null // Clear profile if user logs out
        }
    }
    LaunchedEffect(authState.userProfile) { // Observe userProfile from AuthViewModel
        currentUserProfile = authState.userProfile
    }


    // Handle logout navigation
    LaunchedEffect(authState.isLoggedIn) {
        if (!authState.isLoggedIn && authState.user == null) {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Micro Jobs", fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            }, actions = {
                IconButton(onClick = {
                    authViewModel.signOut()
                    // Toast is handled by LaunchedEffect on authState.isLoggedIn
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout"
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }, floatingActionButton = {
        if (currentUserProfile?.role == "Employer") {
            FloatingActionButton(
                onClick = { navController.navigate("createJob") },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, "Create Job", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    workViewModel.searchJobs(it) // Trigger search
                },
                placeholder = { Text("Search Jobs by Name or Type") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (workListState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (workListState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${workListState.error}", color = MaterialTheme.colorScheme.error)
                }
            } else if (workListState.jobs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No jobs available at the moment.",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Increased spacing
                ) {
                    items(workListState.jobs, key = { it.work_id }) { work ->
                        val isCurrentUserEmployer = authState.userProfile?.role == "Employer"
                        JobCardView(
                            work = work, onClick = {
                                navController.navigate("jobDetail/${work.work_id}")
                            }, isEmployer = isCurrentUserEmployer, // Pass employer status
                            onViewApplicantsClick = if (isCurrentUserEmployer) { // Provide action only if employer
                                { navController.navigate("applicants/${work.work_id}") }
                            } else {
                                null
                            })
                    }
                }
            }
        }
    }
}