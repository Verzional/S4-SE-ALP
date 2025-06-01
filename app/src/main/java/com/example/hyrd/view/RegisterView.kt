package com.example.hyrd.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hyrd.R
import com.example.hyrd.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterView(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") } // Keep as String: "dd/MM/yyyy"
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Employee") } //
    var agreedToTerms by remember { mutableStateOf(false) } //

    val authState by authViewModel.uiState.collectAsState()
    val registrationSuccess by authViewModel.registrationSuccess.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
            // Navigate to login or a verification screen, and clear back stack from register
            navController.navigate("login") { // Or "faceVerification" etc.
                popUpTo("register") { inclusive = true }
            }
            authViewModel.resetRegistrationSuccess() // Reset the flag
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize() //
            .background(Color.White) //
            .padding(32.dp) //
            .verticalScroll(rememberScrollState()), // Make the column scrollable
        horizontalAlignment = Alignment.CenterHorizontally, //
        verticalArrangement = Arrangement.Center //
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, //
            horizontalArrangement = Arrangement.Center, //
            modifier = Modifier.padding(bottom = 24.dp) //
        ) {
            Image(
                painter = painterResource(R.drawable.rectangle_20__1_), //
                contentDescription = null,
                modifier = Modifier.size(100.dp) //
            )
            Spacer(modifier = Modifier.width(16.dp)) //
            Column {
                Text(
                    text = "Let's Get Started!", //
                    fontSize = 24.sp, //
                    fontWeight = FontWeight.Bold, //
                    color = Color.Black, //
                    modifier = Modifier.padding(bottom = 8.dp) //
                )
                Text(
                    text = "Get started in just a few steps!", fontSize = 14.sp, color = Color.Gray //
                )
            }
        }

        OutlinedTextField(
            value = fullName, //
            onValueChange = { fullName = it },
            label = { Text("Full Name") }, //
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp), //
            singleLine = true
        )

        OutlinedTextField(
            value = phoneNumber, //
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") }, //
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), //
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp), //
            singleLine = true
        )

        OutlinedTextField(
            value = dateOfBirth, //
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of Birth (dd/MM/yyyy)") }, //
            placeholder = { Text("dd/MM/yyyy")},
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp), //
            singleLine = true
        )

        OutlinedTextField(
            value = email, //
            onValueChange = { email = it },
            label = { Text("Email") }, //
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), //
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp), //
            singleLine = true
        )

        OutlinedTextField(
            value = password, //
            onValueChange = { password = it },
            label = { Text("Password") }, //
            visualTransformation = PasswordVisualTransformation(), //
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), //
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp), //
            singleLine = true
        )

        Row(
            verticalAlignment = Alignment.CenterVertically, //
            horizontalArrangement = Arrangement.Start, //
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp) //
        ) {
            RadioButton(
                selected = selectedRole == "Employee", //
                onClick = { selectedRole = "Employee" }, //
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2F7CF6), unselectedColor = Color.Gray //
                )
            )
            Text("I'm an Employee", modifier = Modifier.padding(start = 4.dp, end = 16.dp).clickable { selectedRole = "Employee" }) //
            Spacer(Modifier.width(8.dp))
            RadioButton(
                selected = selectedRole == "Employer", //
                onClick = { selectedRole = "Employer" }, //
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2F7CF6), unselectedColor = Color.Gray //
                )
            )
            Text("I'm an Employer", modifier = Modifier.padding(start = 4.dp).clickable { selectedRole = "Employer" }) //
        }


        Row(
            verticalAlignment = Alignment.CenterVertically, //
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp) //
        ) {
            Checkbox(
                checked = agreedToTerms, onCheckedChange = { agreedToTerms = it }) //
            Text(
                text = "I agree to all the ", fontSize = 12.sp //
            )
            Text(
                text = "Terms ", fontSize = 12.sp, color = Color(0xFFFFDBA3) //
            )
            Text(
                text = "and ", fontSize = 12.sp //
            )
            Text(
                text = "Privacy Policies", fontSize = 12.sp, color = Color(0xFFFFDBA3) //
            )
        }
        if (authState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        } else {
            Button(
                onClick = {
                    if (!agreedToTerms) {
                        Toast.makeText(context, "You must agree to the terms and privacy policies.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (fullName.isBlank() || phoneNumber.isBlank() || dateOfBirth.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    authViewModel.registerUser(
                        fullName,
                        phoneNumber,
                        dateOfBirth,
                        email,
                        password,
                        selectedRole
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7CF6)), //
                modifier = Modifier
                    .fillMaxWidth() //
                    .padding(bottom = 16.dp) //
            ) {
                Text(text = "Next", color = Color.White) //
            }
        }

        Row {
            Text(
                text = "Already have an account?", fontSize = 14.sp, color = Color.Black //
            )
            Spacer(modifier = Modifier.width(4.dp)) //
            Text(
                text = "Login", fontSize = 14.sp, color = Color(0xFFFFDBA3), modifier = Modifier.clickable { //
                    navController.navigate("login") //
                }
            )
        }
    }
}