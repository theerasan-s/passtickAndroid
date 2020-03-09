package coe.project.passtick

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Shops (
    val cost: Long?=0,
    val donate: Long?=0,
    val lat: Long?=0,
    val lng: Long?=0,
    val logo: String?="",
    val outstanding: Long?=0,
    val pieces: Long?=0,
    val shoplink: String?="",
    val tel: String?="",
    var shopName: String=""
)