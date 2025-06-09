package com.example.hyrd_v2.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginView(navController) }
        composable("register") { RegisterView(navController) }

        composable("faceVerification") { FaceVerificationView(navController) }
        composable("idCardVerification") { IDCardVerificationView(navController) }
        composable("verified") { VerifiedView(navController) }

        composable("jobList") { JobListView(navController = navController) }

        composable(
            route = "jobDetail/{workId}",
            arguments = listOf(navArgument("workId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId")
            if (workId != null) {
                JobDetailView(workId = workId, navController = navController)
            } else {
                Text("Error: Job ID missing.") // Basic error handling
            }
        }

        composable("createJob") { CreateJobView(navController) }
        composable("successfulApply") { SuccessfulApplyView(navController) }

        // ApplicantListView now takes workId
        composable(
            route = "applicants/{workId}",
            arguments = listOf(navArgument("workId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId")
            if (workId != null) {
                ApplicantListView(navController = navController, workId = workId)
            } else {
                Text("Error: Work ID missing for applicants list.") // Basic error handling
            }
        }
    }
}