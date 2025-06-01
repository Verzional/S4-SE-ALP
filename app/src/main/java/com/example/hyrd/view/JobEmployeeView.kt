package com.example.hyrd.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hyrd.model.JobModel
import com.example.hyrd.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobEmployeeView(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val jobs = listOf(
        JobModel(
            "Product Data Entry",
            "IDR 50,000 per 100 products",
            listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        ), JobModel(
            "Product Data Entry",
            "IDR 50,000 per 100 products",
            listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        ), JobModel(
            "Product Data Entry",
            "IDR 50,000 per 100 products",
            listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        ), JobModel(
            "Product Data Entry",
            "IDR 50,000 per 100 products",
            listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        )
    )

    var searchQuery by remember { mutableStateOf("") }
    val authState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Observe authState to redirect if not logged in
    LaunchedEffect(authState) {
        if (!authState.isLoggedIn && authState.user == null) { // Check specifically if user is null after sign out
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Micro Jobs",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.signOut()
                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // Or your desired app bar color
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding) // Apply padding from Scaffold
                .padding(horizontal = 16.dp) // Add your own horizontal padding
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Space after TopAppBar

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Job") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(jobs.filter {
                    it.title.contains(
                        searchQuery,
                        ignoreCase = true
                    ) || it.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
                }) { job ->
                    JobCardView(job = job) // Assuming JobCardView is clickable and navigates if needed
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
        }
    }
}
