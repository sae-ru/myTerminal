package com.example.myterminal.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//with wrong field order
@JsonClass(generateAdapter = true)
data class OCRPassportFieldsResponse(
    @Json(name = "bdate")
    val passportBirthday: String,
    @Json(name = "id")
    val passportID: String,
    @Json(name = "surname")
    val passportSurname: String,
    @Json(name = "secondname")
    val passportPatronymic: String,
    @Json(name = "name")
    val passportName: String,
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
        ‘data’ : ‘<Строка base64>’
    }
*/

/*
    /*
        {
            'bdate': '23062о02',
            'id': '022197659',
            'surname': 'морозовп',
            'secondname': 'денисови',
            'name': 'иван',
            'gender': 'муж',
            'image': b'/9j/4AAQSkZJ…’}
        {
     */


    //with preferred field order
    @JsonClass(generateAdapter = true)
    data class OCRPassportFieldsResponse(
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


    //with wrong field order
    @JsonClass(generateAdapter = true)
    data class OCRPassportFieldsResponse(
        @Json(name = "bdate")
        val passportBirthday: String,
        @Json(name = "id")
        val passportID: String,
        @Json(name = "surname")
        val passportSurname: String,
        @Json(name = "secondname")
        val passportPatronymic: String,
        @Json(name = "name")
        val passportName: String,
        @Json(name = "gender")
        val passportGender: String,
        @Json(name = "image")
        val passportFacePhoto: String
    )
 */