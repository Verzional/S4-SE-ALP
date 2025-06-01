package com.example.hyrd.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
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
import com.example.hyrd.model.WorkModel

@Composable
fun JobCardView(work: WorkModel, onClick: () -> Unit) { // Changed parameter to WorkModel
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Use the passed onClick lambda
            .padding(vertical = 16.dp, horizontal = 8.dp), // Added some horizontal padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            Text(
                text = work.name, // Changed from job.title to work.name
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "IDR ${"%,.0f".format(work.wage)}", // Changed from job.price to work.wage and formatted
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = work.job_type, // Assuming job_type is like a category/tag
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Location: ${work.location}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Icon(
            painter = painterResource(R.drawable.outline_arrow_forward_ios_24),
            contentDescription = "View Details",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun TagBadge(text: String) { // This can be kept if you have other uses or adapt it
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
        work = WorkModel( // Changed to WorkModel for preview
            work_id = "1",
            name = "Product Data Entry Expert",
            description = "Detailed data entry job.",
            job_type = "Data Entry",
            quota = 5,
            location = "Remote",
            work_hour = "Flexible",
            wage = 50000.0,
            status = true
        ),
        onClick = {}
    )
}