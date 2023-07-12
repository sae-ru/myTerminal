package com.example.myterminal.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthTokenRequest(
    @Json(name = "login")
    val login: String,
    @Json(name = "password")
    val password: String
//    ,
//    @Json(name = "imei")
//    val imei: String,
//    @Json(name = "card")
//    val card: String
)
