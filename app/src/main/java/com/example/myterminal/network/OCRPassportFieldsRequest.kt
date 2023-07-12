package com.example.myterminal.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OCRPassportFieldsRequest(
    @Json(name = "data")
    val data: String
)

/*
    {
        'id': '022197659',
        'surname': 'морозовп',
        'name': 'иван',
        'secondname': 'денисови',
        'bdate': '23062о02',
        'gender': 'муж',
        'image': b'/9j/4AAQSkZJ…’
    }
    {
        ‘data’ : ‘<Строка base64>’
    }
*/
