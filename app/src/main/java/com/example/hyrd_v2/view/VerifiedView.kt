package com.example.hyrd_v2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import com.example.hyrd_v2.R
import kotlinx.coroutines.delay

@Composable
fun VerifiedView(navController: NavController) {
    // Auto-redirect after a delay
    LaunchedEffect(Unit) {
        delay(3000) // 3-second delay (adjust as needed)
        navController.navigate("jobList") {
            // Clear the entire verification flow off the back stack,
            // making jobList the new "base" after login/registration.
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true // Avoid multiple copies of jobList
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.verified), // Ensure this drawable exists
                contentDescription = "Verified Icon", modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Successfully Verified!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your account is now verified. Redirecting you to the dashboard...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray, // Or MaterialTheme.colorScheme.onSurfaceVariant
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Optional: You can add a CircularProgressIndicator while waiting for the redirect
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Helper function to find the start destination of the graph.
// Consider moving this to a common utility file if used in multiple places.
private fun NavGraph.findStartDestination(): NavDestination {
    var startDestination: NavDestination = this
    while (startDestination is NavGraph) {
        startDestination =
            (startDestination as NavGraph).findNode((startDestination as NavGraph).startDestinationId)!!
    }
    return startDestination
}