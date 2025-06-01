package com.example.hyrd.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hyrd.model.ApplicantModel

@Composable
fun ApplicantListView(navController: NavController, onBack: () -> Unit = {}) {
    val initialApplicants = remember {
        listOf(
            ApplicantModel(
                "Olivia Hartono",
                25,
                "olivia.hartono@example.com",
                "+62 812-3456-7890",
                "olivia_cv.pdf",
                ApplicantStatus.Waiting
            ), ApplicantModel(
                "Olivia Hartono",
                25,
                "olivia.hartono@example.com",
                "+62 812-3456-7890",
                "olivia_cv.pdf",
                ApplicantStatus.Rejected
            ), ApplicantModel(
                "Olivia Hartono",
                25,
                "olivia.hartono@example.com",
                "+62 812-3456-7890",
                "olivia_cv.pdf",
                ApplicantStatus.Approved
            ), ApplicantModel(
                "Olivia Hartono",
                25,
                "olivia.hartono@example.com",
                "+62 812-3456-7890",
                "olivia_cv.pdf",
                ApplicantStatus.Approved
            ), ApplicantModel(
                "Olivia Hartono",
                25,
                "olivia.hartono@example.com",
                "+62 812-3456-7890",
                "olivia_cv.pdf",
                ApplicantStatus.Approved
            )
        )
    }

    val applicantStates = remember { initialApplicants.map { it.copy() }.toMutableStateList() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Product Data Entry", fontWeight = FontWeight.Bold, fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            itemsIndexed(applicantStates) { index, applicant ->
                ApplicantCardView(
                    applicant = applicant, onStatusChange = { newStatus ->
                        applicantStates[index] = applicant.copy(status = newStatus)
                    })
            }
        }
    }
}