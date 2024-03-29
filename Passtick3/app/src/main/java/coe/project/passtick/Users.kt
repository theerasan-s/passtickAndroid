package coe.project.passtick

import com.google.firebase.database.IgnoreExtraProperties
import java.nio.channels.spi.AbstractSelectionKey


@IgnoreExtraProperties
data class Users (
    val username: String? = "",
    val email: String? = "",
    val password: String? = "",
    val fname: String? = "",
    val lname: String? = "",
    val profile: String? = "",
    val role: String? = "customer",
    val save: Long? = 0,
    var rank: Int=0,
    var key: String =""
)