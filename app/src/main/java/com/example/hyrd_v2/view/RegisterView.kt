package com.example.hyrd_v2.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hyrd_v2.R
import com.example.hyrd_v2.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
    var selectedRole by remember { mutableStateOf("Employee") }
    var agreedToTerms by remember { mutableStateOf(false) }

    val authState by authViewModel.uiState.collectAsState()
    val registrationSuccess by authViewModel.registrationSuccess.collectAsState()

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            Toast.makeText(
                context,
                "Registration Successful! Proceed to verification.",
                Toast.LENGTH_LONG
            ).show()
            // Navigate to faceVerification and clear back stack from register
            navController.navigate("faceVerification") { // Changed from "login"
                popUpTo("register") { inclusive = true } // Clear this screen
            }
            authViewModel.resetRegistrationSuccess() // Reset the flag
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            if (!authState.isLoading) { // Avoid showing error if still loading
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                // Consider adding authViewModel.clearError() if you want errors to be one-time
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Your Account", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp) // Adjusted padding
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.rectangle_20__1_), // Ensure this drawable exists
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(80.dp) // Slightly smaller logo
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Let's Get Started!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Join our community in just a few steps.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Personal Information
            SectionTitle("Personal Information")
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number *") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Date of Birth (dd/MM/yyyy) *") },
                placeholder = { Text("dd/MM/yyyy") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                ), // Using Uri for / char
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Account Details
            SectionTitle("Account Details")
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email *") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password *") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Role Selection
            SectionTitle("I am an...")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                RoleRadioButton("Employee", selectedRole == "Employee") {
                    selectedRole = "Employee"
                }
                RoleRadioButton("Employer", selectedRole == "Employer") {
                    selectedRole = "Employer"
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Terms and Conditions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text("I agree to all the ", style = MaterialTheme.typography.bodySmall)
                    Row {
                        Text(
                            text = "Terms ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { /* TODO: Navigate to Terms */ }
                        )
                        Text("and ", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "Privacy Policies",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { /* TODO: Navigate to Privacy Policy */ }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (authState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            } else {
                Button(
                    onClick = {
                        // Validation
                        if (!agreedToTerms) {
                            Toast.makeText(
                                context,
                                "You must agree to the terms and privacy policies.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (fullName.isBlank() || phoneNumber.isBlank() || dateOfBirth.isBlank() || email.isBlank() || password.isBlank()) {
                            Toast.makeText(
                                context,
                                "Please fill all required (*) fields.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        // Validate date format (basic)
                        if (!dateOfBirth.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
                            Toast.makeText(
                                context,
                                "Date of Birth must be in dd/MM/yyyy format.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        authViewModel.registerUser(
                            fullName, phoneNumber, dateOfBirth, email, password, selectedRole
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Register & Proceed to Verification",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(bottom = 24.dp), // Extra padding at the bottom
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate("login") {
                            popUpTo("register") {
                                inclusive = true
                            } // Clear register if navigating to login
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 4.dp), // Added top padding
        textAlign = TextAlign.Start, // Align to start for a cleaner look
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun RoleRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp) // Padding for clickable area
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick, // RadioButton itself can also handle the click
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}