package com.example.myterminal.network

import com.squareup.moshi.Json

data class PassportOCRFields(
    @Json(name = "id")
    val passportID: String,
    @Json(name = "surname")
    val passportSurname: String,
    @Json(name = "name")
    val passportName: String,
    @Json(name = "secondname")
    val passportPatronymic: String,
    @Json(name = "bdate")
    val passportBirthday: String,
    @Json(name = "gender")
    val passportGender: String,
    @Json(name = "image")
    val passportFacePhoto: String
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