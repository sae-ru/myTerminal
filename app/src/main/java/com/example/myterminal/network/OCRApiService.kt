package com.example.myterminal.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

//ngrok http 8000
var OCR_SERVICE_URL = "c57e-31-134-188-16.ngrok-free.app" //192.168.1.83:8080

private val okhttpClient = OkHttpClient.Builder()
    .readTimeout(10, TimeUnit.SECONDS)
    .connectTimeout(10, TimeUnit.SECONDS)
    .build()

private val moshiOCR = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitForOCR = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiOCR))
    .baseUrl("https://$OCR_SERVICE_URL/")
    .client(okhttpClient)
    .build()

interface OCRApiService {
    @POST("echo")
    suspend fun postPassportImage(@Body passportImage: OCRPassportFieldsRequest): OCRPassportFieldsResponse
}

object OCRApi {
    val retrofitOCRService: OCRApiService by lazy {
        retrofitForOCR.create(OCRApiService::class.java)
    }
}
