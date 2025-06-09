package com.example.hyrd_v2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hyrd_v2.R

@Composable
fun SuccessfulApplyView(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Use theme background
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.variant8), // Ensure this drawable exists
            contentDescription = "Success",
            tint = Color(0xFF00B389), // Custom success color
            modifier = Modifier.size(100.dp) // Increased size
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Application Submitted!", // More specific title
            style = MaterialTheme.typography.headlineMedium, // Use Material typography
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your application has been sent. The employer will review it, and you'll be notified of their decision.",
            style = MaterialTheme.typography.bodyMedium, // Use Material typography
            color = Color.Gray, // Or MaterialTheme.colorScheme.onSurfaceVariant
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.85f) // Constrain width for readability
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate("jobList") {
                    // Pop up to jobList to avoid going back to successfulApply or jobDetail
                    // Or pop up to the start destination if preferred
                    popUpTo("jobList") {
                        inclusive = false
                    } // Go to jobList, don't include jobList itself in pop if already there
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Back to Job Listings", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}