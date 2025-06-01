package com.example.hyrd.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hyrd.R

@Composable
fun FaceVerificationView(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "Back",
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Identity Verification", fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Face Verification", fontWeight = FontWeight.Bold, fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your personal data is guaranteed to be safe in accordance with the Terms of Service and Privacy Policy of each party to comply with the regulations of the relevant authorities.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF5E6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤", fontSize = 64.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Facial recognition", fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "In order to improve the success rate of face recognition, please follow these requirements below",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FacialRequirement(
                    icon = painterResource(R.drawable.ic2_iphone), label = "Hold phone upright"
                )
                FacialRequirement(
                    icon = painterResource(R.drawable.sun), label = "Well-lit"
                )
                FacialRequirement(
                    icon = painterResource(R.drawable.ic2_password2), label = "Don't occluded face"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* TODO: Add navigation or action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7CF6)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Next", color = Color.White)
            }
        }
    }
}

@Composable
fun FacialRequirement(icon: Painter, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = icon, contentDescription = label, modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center
        )
    }
}