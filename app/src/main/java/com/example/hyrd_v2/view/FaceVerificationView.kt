package com.example.hyrd_v2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hyrd_v2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceVerificationView(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Identity Verification", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Typically, you might not want a back button in a sequential flow
                    // or it should go to a specific point (e.g., app exit or start of flow).
                    // For now, let's keep it simple or remove if it complicates flow.
                    // IconButton(onClick = { navController.popBackStack() }) { // Or handle back more carefully
                    //     Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    // }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Face Verification",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Your personal data is guaranteed to be safe. Please follow the requirements below for successful face recognition.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .size(150.dp) // Increased size
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                    .padding(16.dp) // Padding inside the circle
                    .clickable { /* TODO: Implement open camera action */ },
                contentAlignment = Alignment.Center
            ) {
                Icon( // Using an icon instead of text emoji
                    painter = painterResource(id = R.drawable.outline_add_a_photo_24), // Placeholder, use a face scan icon
                    contentDescription = "Take Face Photo",
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = "Tap to Open Camera",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )


            Text(
                text = "Facial Recognition Tips",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FacialRequirement( // Ensure these drawables exist
                    icon = painterResource(R.drawable.ic2_iphone), label = "Hold phone upright"
                )
                FacialRequirement(
                    icon = painterResource(R.drawable.sun),
                    label = "Ensure good lighting" // Clearer text
                )
                FacialRequirement(
                    icon = painterResource(R.drawable.ic2_password2),
                    label = "No face obstructions" // Clearer text
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Push button to bottom

            Button(
                onClick = {
                    // TODO: Add actual face verification logic if any, or check if photo was taken
                    navController.navigate("idCardVerification") // Navigate to ID Card Verification
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Next: ID Card Verification", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FacialRequirement(icon: Painter, label: String) { // Already exists, ensure it's styled well
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp) // Give some fixed width for better arrangement
    ) {
        Icon( // Changed from Image to Icon for consistency if they are vector drawables
            painter = icon,
            contentDescription = label,
            modifier = Modifier.size(36.dp), // Slightly larger
            tint = MaterialTheme.colorScheme.secondary // Themed tint
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}