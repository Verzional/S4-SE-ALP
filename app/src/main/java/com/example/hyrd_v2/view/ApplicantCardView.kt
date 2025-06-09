package com.example.hyrd_v2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hyrd_v2.R
import com.example.hyrd_v2.model.ApplicationModel
import java.text.SimpleDateFormat
import java.util.Locale

// Status Enum - can be kept for local styling decisions or mapping
enum class ApplicantDisplayStatus(val label: String, val color: Color) {
    Pending("Pending", Color(0xFFFFD873)), // Yellowish
    Rejected("Rejected", Color(0xFFFF7D87)),  // Reddish
    Accepted("Accepted", Color(0xFF0FBE97)); // Greenish

    companion object {
        fun fromString(status: String): ApplicantDisplayStatus {
            return when (status.lowercase(Locale.getDefault())) {
                "accepted" -> Accepted
                "rejected" -> Rejected
                else -> Pending
            }
        }
    }
}

@Composable
fun ApplicantCardView(
    application: ApplicationModel, // Changed from ApplicantModel to ApplicationModel
    onStatusChange: (newStatus: String) -> Unit // newStatus will be "accepted" or "rejected"
) {
    var expanded by remember { mutableStateOf(false) }
    val displayStatus = ApplicantDisplayStatus.fromString(application.status)
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }


    Card( // Using Card for better elevation and structure
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) { // Align to top for status badge
                Image( // Placeholder for applicant image
                    painter = painterResource(R.drawable._3b43f1309d921872741ed31a3676b0e), // Ensure this placeholder exists
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp) // Slightly larger
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = application.applicant_name.ifEmpty { "N/A" }, // Use denormalized name
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = application.applicant_email.ifEmpty { "N/A" }, // Use denormalized email
                        style = MaterialTheme.typography.bodySmall, color = Color.Gray
                    )
                    application.application_date?.let {
                        Text(
                            text = "Applied: ${dateFormatter.format(it)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    if (application.cv_path.isNotBlank()) {
                        Text( // Placeholder for CV link
                            text = "CV: ${application.cv_path.substringAfterLast('/')}", // Show filename
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { /* TODO: Implement CV download/view */ })
                    }
                }

                // Status Badge and Menu
                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .background(
                                displayStatus.color, shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = displayStatus.label,
                            color = Color.White, // Assuming white text looks good on these badge colors
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (application.status.equals(
                            "pending", ignoreCase = true
                        )
                    ) { // Only show menu for pending
                        Spacer(modifier = Modifier.height(4.dp))
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    Icons.Filled.MoreVert, contentDescription = "Manage Application"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(text = { Text("Accept Application") }, onClick = {
                                    onStatusChange("accepted")
                                    expanded = false
                                })
                                DropdownMenuItem(text = { Text("Reject Application") }, onClick = {
                                    onStatusChange("rejected")
                                    expanded = false
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun PreviewApplicantCard() {
    var status by remember { mutableStateOf("pending") }
    ApplicantCardView(
        application = ApplicationModel(
            application_id = "app123",
            work_id = "work789",
            worker_id = "user456",
            applicant_name = "Olivia Hartono",
            applicant_email = "olivia.h@example.com",
            cv_path = "cv_uploads/olivia_cv.pdf",
            application_date = java.util.Date(),
            status = status
        ), onStatusChange = { newStatus -> status = newStatus })
}