package com.example.hyrd.model

import com.google.firebase.firestore.PropertyName
import java.util.Date

data class ApplicationModel(
    @PropertyName("worker_id") val worker_id: String = "",
    @PropertyName("work_id") val work_id: String = "",
    @PropertyName("application_date") val application_date: Date? = null,
    @PropertyName("status") val status: String = "pending",
    @PropertyName("review_notes") val review_notes: String = "",
    @PropertyName("attached_cv") val attached_cv: String = ""
) {
    constructor() : this("", "", null, "pending", "", "")
}