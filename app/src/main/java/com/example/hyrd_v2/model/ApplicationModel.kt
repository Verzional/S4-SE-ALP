package com.example.hyrd_v2.model

import com.google.firebase.database.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ApplicationModel(
    @PropertyName("application_id") val application_id: String = "", // Firestore Document ID
    @PropertyName("work_id") val work_id: String = "",                // ID of the job applied for
    @PropertyName("worker_id") val worker_id: String = "",            // ID of the user (employee) who applied
    @PropertyName("applicant_name") val applicant_name: String = "",   // Name of the applicant (denormalized)
    @PropertyName("applicant_email") val applicant_email: String = "", // Email of the applicant (denormalized)
    @PropertyName("cv_path") val cv_path: String = "",                // Path or reference to the CV (e.g., Firebase Storage path)
    @PropertyName("application_date") @ServerTimestamp val application_date: Date? = null, // Managed by Firestore
    @PropertyName("status") var status: String = "pending",        // "pending", "accepted", "rejected"
    @PropertyName("review_notes") val review_notes: String = ""       // Optional notes from employer
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        application_id = "",
        work_id = "",
        worker_id = "",
        applicant_name = "",
        applicant_email = "",
        cv_path = "",
        application_date = null,
        status = "pending",
        review_notes = ""
    )
}