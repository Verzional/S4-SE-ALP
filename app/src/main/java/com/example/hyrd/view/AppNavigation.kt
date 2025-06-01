package com.example.hyrd.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hyrd.model.JobModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginView(navController) }
        composable("register") { RegisterView(navController) }
        composable("faceVerification") { FaceVerificationView(navController) }
        composable("idCardVerification") { IDCardVerificationView(navController) }
        composable("verified") { VerifiedView(navController) }
        composable("jobEmployee") { JobEmployeeView(navController) }
        composable("jobEmployer") { JobEmployerView(navController) }
        composable("jobDetail") {
            JobDetailView(
                job = JobModel(
                    title = "Product Data Entry",
                    price = "IDR 50,000 per 100 products",
                    tags = listOf("Microsoft Excel", "High Attention", "Basic e-commerce knowledge")
                ), navController
            )
        }
        composable("successfulApply") { SuccessfulApplyView(navController) }
        composable("createJob") { CreateJobView(navController) }
        composable("applicants") { ApplicantListView(navController) }
    }
}