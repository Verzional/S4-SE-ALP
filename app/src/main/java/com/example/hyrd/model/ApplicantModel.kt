package com.example.hyrd.model

import com.example.hyrd.view.ApplicantStatus

data class ApplicantModel(
    val name: String,
    val age: Int,
    val email: String,
    val phone: String,
    val resumeUrl: String,
    val status: ApplicantStatus
)