package com.example.hyrd_v2.model

import com.google.firebase.database.PropertyName

data class WorkModel(
    @PropertyName("work_id") val work_id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("description") val description: String = "",
    @PropertyName("job_type") val job_type: String = "",
    @PropertyName("quota") val quota: Int = 0,
    @PropertyName("location") val location: String = "",
    @PropertyName("work_hour") val work_hour: String = "",
    @PropertyName("wage") val wage: Double = 0.0,
    @PropertyName("status") val status: Boolean = true
) {
    constructor() : this("", "", "", "", 0, "", "", 0.0, true)
}