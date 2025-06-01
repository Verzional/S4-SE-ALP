package com.example.hyrd.view

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hyrd.model.JobModel

@Composable
fun JobEmployeeView(navController: NavController) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Micro Jobs", fontWeight = FontWeight.Bold, fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
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
            items(jobs) { job ->
                JobCardView(job = job)
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}