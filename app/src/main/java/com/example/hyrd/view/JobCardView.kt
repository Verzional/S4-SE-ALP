package com.example.hyrd.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hyrd.R
import com.example.hyrd.model.JobModel

@Composable
fun JobCardView(job: JobModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 25.dp)
            .clickable { /* Handle click */ }, verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            Text(
                text = job.title, fontWeight = FontWeight.Bold, fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = job.price, fontSize = 14.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                job.tags.forEach { tag ->
                    TagBadge(text = tag)
                }
            }
        }

        Icon(
            painter = painterResource(R.drawable.outline_arrow_forward_ios_24),
            contentDescription = "Go",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun TagBadge(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFE0B2), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text, fontSize = 10.sp, color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JobCardPreview() {
    JobCardView(
        job = JobModel(
            title = "Product Data Entry",
            price = "IDR 50,000 per 100 products",
            tags = listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
        )
    )
}