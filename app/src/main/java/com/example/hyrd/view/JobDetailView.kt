package com.example.hyrd.view

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hyrd.R
import com.example.hyrd.model.JobModel

@Composable
fun JobDetailView(job: JobModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { /* Handle back */ })
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = job.title, fontWeight = FontWeight.Bold, fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Job details
        Text(
            text = "Enter product information from an Excel sheet into an e-commerce website, including name, price, description, and image.",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Wage: ${job.price}", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Location: Remote", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Work Hours: Flexible, approximately 2–3 hours", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Skills:", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            job.tags.forEach { tag ->
                TagBadge(text = tag)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Quota: 3 workers", fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Handle upload */ },
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
                    text = "Formats accepted are .pdf", fontSize = 12.sp, color = Color.Gray
                )
                Text(
                    text = "*required", fontSize = 10.sp, color = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Handle apply */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7CF6)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Apply Job",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}