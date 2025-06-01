package com.example.hyrd.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.hyrd.model.JobModel

@Composable
fun JobEmployerView(navController: NavController) {
    val jobs = listOf(
        JobModel(
            "Product Data Entry",
            "IDR 50,000 per 100 products",
            listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        ),
        JobModel(
            "Product Data Entry",
            "IDR 50,000 per 100 products",
            listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        ),
        JobModel(
            "Product Data Entry",
            "IDR 50,000 per 100 products",
            listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        ),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Micro Jobs", fontWeight = FontWeight.Bold, fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(jobs) { job ->
                    JobCardView(job)
                }
            }
        }

        FloatingActionButton(
            onClick = { /* Handle action */ },
            containerColor = Color(0xFF4A90E2),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .zIndex(1f)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Job")
        }
    }
}