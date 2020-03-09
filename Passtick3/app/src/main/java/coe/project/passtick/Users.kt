package coe.project.passtick

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Users (
    val username: String? = "",
    val email: String? = "",
    val password: String? = "",
    val fname: String? = "",
    val lname: String? = "",
    val profile: String? = "",
    val role: String? = "customer",
    val save: Long? = 0
)