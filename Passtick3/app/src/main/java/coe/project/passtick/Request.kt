package coe.project.passtick

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Request(
    var user:String = "ผู้ใช้ทั่วไป",
    var profile:String = "https://firebasestorage.googleapis.com/v0/b/passtick2.appspot.com/o/profile%2Fprofile.png?alt=media&token=fc4c6e8d-829f-4e70-b49f-590e7246fcdf",
    var key:String = "no",
    val time:Long? = 0,
    val pieces: Int? = 0
)