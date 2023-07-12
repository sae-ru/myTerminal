package com.example.myterminal.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthTokenResponse(
    @Json(name = "token")
    val token: String
//    ,
//    @Json(name = "refresh_token")
//    val refreshToken: String
)
