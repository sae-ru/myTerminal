package com.example.myterminal.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

//TODO: find service's url
var OCR_SERVICE_URL = "localhost:90"

private val moshiOCR = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitForOCR = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiOCR))
    .baseUrl("https://$OCR_SERVICE_URL/")
    .build()

interface OCRApiService {
    @POST("data")
    suspend fun postPassportImage(@Body passportImage: Map<String, String>): Call<PassportOCRFields>
}

object OCRApi {
    val retrofitOCRService : OCRApiService by lazy {
        retrofitForOCR.create(OCRApiService::class.java) }
}
