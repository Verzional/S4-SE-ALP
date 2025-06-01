package com.example.hyrd.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hyrd.R

@Composable
fun RegisterView(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Employee") }
    var agreedToTerms by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.rectangle_20__1_),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Let's Get Started!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Get started in just a few steps!", fontSize = 14.sp, color = Color.Gray
                )
            }
        }

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of Birth") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            RadioButton(
                selected = selectedRole == "Employee",
                onClick = { selectedRole = "Employee" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2F7CF6), unselectedColor = Color.Gray
                )
            )
            Text("I'm an Employee", modifier = Modifier.padding(end = 16.dp))
            RadioButton(
                selected = selectedRole == "Employer",
                onClick = { selectedRole = "Employer" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2F7CF6), unselectedColor = Color.Gray
                )
            )
            Text("I'm an Employer")
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Checkbox(
                checked = agreedToTerms, onCheckedChange = { agreedToTerms = it })
            Text(
                text = "I agree to all the ", fontSize = 12.sp
            )
            Text(
                text = "Terms ", fontSize = 12.sp, color = Color(0xFFFFDBA3)
            )
            Text(
                text = "and ", fontSize = 12.sp
            )
            Text(
                text = "Privacy Policies", fontSize = 12.sp, color = Color(0xFFFFDBA3)
            )
        }

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7CF6)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Next", color = Color.White)
        }

        Row {
            Text(
                text = "Already have an account?", fontSize = 14.sp, color = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Login", fontSize = 14.sp, color = Color(0xFFFFDBA3), modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )
        }
    }
}