package com.example.hyrd.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
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
import com.example.hyrd.R
import com.example.hyrd.model.ApplicantModel

// Status Enum
enum class ApplicantStatus(val label: String, val color: Color) {
    Waiting("Waiting", Color(0xFFFFD873)), Rejected(
        "Rejected", Color(0xFFFF7D87)
    ),
    Approved("Approved", Color(0xFF0FBE97))
}

@Composable
fun ApplicantCardView(
    applicant: ApplicantModel, onStatusChange: (ApplicantStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable._3b43f1309d921872741ed31a3676b0e),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = applicant.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${applicant.age} years old", fontSize = 12.sp, color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = applicant.email, fontSize = 12.sp, color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = applicant.phone, fontSize = 12.sp, color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = applicant.resumeUrl, fontSize = 14.sp, color = Color(0xFF4A90E2)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(
                        applicant.status.color, shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = applicant.status.label, color = Color.White, fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box {
            OutlinedButton(
                onClick = { expanded = true }) {
                Text(
                    text = "Manage", color = Color(0xFF2F7CF6)
                )
            }

            DropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }) {
                ApplicantStatus.values().forEach { status ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = status.label,
                                color = if (status == ApplicantStatus.Waiting) Color.Gray else Color.Black
                            )
                        }, onClick = {
                            if (status != ApplicantStatus.Waiting) {
                                onStatusChange(status)
                            }
                            expanded = false
                        }, enabled = status != ApplicantStatus.Waiting
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApplicantCard() {
    var status by remember { mutableStateOf(ApplicantStatus.Waiting) }

    ApplicantCardView(
        applicant = ApplicantModel(
            name = "Olivia Hartono",
            age = 25,
            email = "olivia.hartono@example.com",
            phone = "+62 812-3456-7890",
            resumeUrl = "olivia_cv.pdf",
            status = status
        ), onStatusChange = { newStatus -> status = newStatus })
}