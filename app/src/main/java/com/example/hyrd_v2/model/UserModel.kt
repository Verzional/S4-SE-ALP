package com.example.hyrd_v2.model

import com.google.firebase.database.PropertyName
import java.util.Date

data class UserModel(
    @PropertyName("user_id") val user_id: String = "",
    @PropertyName("role") val role: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("email") val email: String = "",
    @PropertyName("password") val password: String = "",
    @PropertyName("phone_number") val phone_number: String = "",
    @PropertyName("date_of_birth") val date_of_birth: Date? = null,
    @PropertyName("bio") val bio: String = "",
    @PropertyName("profile_picture") val profile_picture: String = "",
    @PropertyName("bio_image") val bio_image: String = ""
) {
    constructor() : this("", "", "", "", "", "", null, "", "", "")
}