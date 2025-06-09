package com.example.hyrd_v2.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ApplicationModel(
    @PropertyName("application_id") val application_id: String = "",
    @PropertyName("work_id") val work_id: String = "",
    @PropertyName("worker_id") val worker_id: String = "",
    @PropertyName("applicant_name") val applicant_name: String = "",
    @PropertyName("applicant_email") val applicant_email: String = "",
    @PropertyName("cv_path") val cv_path: String = "",
    @PropertyName("application_date") @ServerTimestamp val application_date: Date? = null,
    @PropertyName("status") var status: String = "pending",
    @PropertyName("review_notes") val review_notes: String = ""
) {
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