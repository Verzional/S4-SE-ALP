package com.example.hyrd.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

// Removed JobModel import as it's a placeholder

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginView(navController) }
        composable("register") { RegisterView(navController) }

        // Identity Verification Flow (Remains the same for now)
        composable("faceVerification") { FaceVerificationView(navController) }
        composable("idCardVerification") { IDCardVerificationView(navController) }
        composable("verified") { VerifiedView(navController) }

        // Consolidated Job List View
        composable("jobList") { JobListView(navController = navController) } // Changed from jobEmployee/jobEmployer

        // Job Detail View - now takes workId as argument
        composable(
            route = "jobDetail/{workId}",
            arguments = listOf(navArgument("workId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId")
            if (workId != null) {
                JobDetailView(workId = workId, navController = navController)
            } else {
                // Handle error or navigate back if workId is missing
                Text("Error: Job ID missing. Please go back.")
            }
        }

        composable("createJob") { CreateJobView(navController) }

        // Application Related Views (Remain for now, will be integrated with ApplicationViewModel later)
        composable("successfulApply") { SuccessfulApplyView(navController) }
        composable("applicants") { ApplicantListView(navController) } // This lists applicants for a job
    }
}