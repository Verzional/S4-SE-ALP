package com.example.hyrd.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
fun LoginView(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        if (authState.isLoggedIn) {
            // Navigate to a different screen upon successful login
            // For example, navigate to a home screen and clear the back stack
            navController.navigate("jobEmployee") { // Or your main screen
                popUpTo("login") { inclusive = true }
            }
        }
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize() //
            .background(Color.White) //
            .padding(32.dp), //
        horizontalAlignment = Alignment.CenterHorizontally, //
        verticalArrangement = Arrangement.Center //
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, //
            horizontalArrangement = Arrangement.Center, //
            modifier = Modifier.padding(bottom = 24.dp) //
        ) {
            Image(
                painter = painterResource(R.drawable.rectangle_20), //
                contentDescription = null,
                modifier = Modifier.size(125.dp) //
            )
            Spacer(modifier = Modifier.width(16.dp)) //
            Column {
                Text(
                    text = "Welcome Back!", //
                    fontSize = 24.sp, //
                    fontWeight = FontWeight.Bold, //
                    color = Color.Black, //
                    modifier = Modifier.padding(bottom = 8.dp) //
                )
                Text(
                    text = "Log in to explore micro job opportunities", //
                    fontSize = 14.sp, //
                    color = Color.Gray //
                )
            }
        }

        OutlinedTextField(
            value = email, //
            onValueChange = { email = it },
            label = { Text("Email") }, //
            modifier = Modifier
                .fillMaxWidth() //
                .padding(bottom = 16.dp), //
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                .padding(bottom = 24.dp), //
            singleLine = true
        )

        if (authState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
        } else {
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        authViewModel.login(email, password)
                    } else {
                        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7CF6)), //
                modifier = Modifier
                    .fillMaxWidth() //
                    .padding(bottom = 16.dp) //
            ) {
                Text(text = "Login", color = Color.White) //
            }
        }

        Row {
            Text(
                text = "Don't have an account?", fontSize = 14.sp, color = Color.Black //
            )
            Spacer(modifier = Modifier.width(4.dp)) //
            Text(
                text = "Register", //
                fontSize = 14.sp, //
                color = Color(0xFFFFDBA3), //
                modifier = Modifier.clickable { //
                    navController.navigate("register") //
                }
            )
        }
    }
}