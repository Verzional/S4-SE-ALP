package com.example.hyrd_v2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.hyrd_v2.R
import com.example.hyrd_v2.model.WorkModel

@Composable
fun JobCardView(
    work: WorkModel,
    onClick: () -> Unit,
    isEmployer: Boolean = false, // To conditionally show employer actions
    onViewApplicantsClick: (() -> Unit)? = null // Action for viewing applicants
) {
    Card( // Using Card for consistency and elevation
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.3f
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = work.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "IDR ${"%,.0f".format(work.wage)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary // Emphasize wage
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = work.job_type,
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
                        text = "Location: ${work.location}", fontSize = 12.sp, color = Color.Gray
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.outline_arrow_forward_ios_24),
                    contentDescription = "View Details",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
            // Employer-specific action button
            if (isEmployer && onViewApplicantsClick != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton( // Using OutlinedButton for secondary action
                    onClick = onViewApplicantsClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    border = BorderStroke(
                        1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "View Applicants Icon",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View Applicants", fontSize = 13.sp)
                }
            }
        }
    }
}

// TagBadge and Preview can remain similar, just ensure preview uses the new JobCardView signature
@Preview(showBackground = true)
@Composable
fun JobCardPreviewWithEmployerAction() {
    JobCardView(
        work = WorkModel(
            work_id = "1",
            name = "Product Data Entry Expert",
            job_type = "Data Entry",
            wage = 50000.0,
            location = "Remote"
        ), onClick = {}, isEmployer = true, onViewApplicantsClick = {})
}