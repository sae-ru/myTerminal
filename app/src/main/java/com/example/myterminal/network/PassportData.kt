package com.example.myterminal.network

import com.squareup.moshi.Json

data class PassportData(
    val id: String,
    //TODO: find json answer keys
    @Json(name = "res") val passportData: String
)