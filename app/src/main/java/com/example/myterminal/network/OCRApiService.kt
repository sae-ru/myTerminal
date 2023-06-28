package com.example.myterminal.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//TODO: find service's url
var OCR_SERVICE_URL = ""

private val moshiOCR = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitForOCR = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiOCR))
    .baseUrl(OCR_SERVICE_URL)
    .build()

interface OCRApiService {
    //TODO: path for post data
    @POST("")
    suspend fun postPassportPhoto(@Body passportPhoto: Map<String, Any>): Call<ResponseBody>

    //TODO: path for get data
    @GET("")
    suspend fun getPassportData() : List<PassportData>
}

object OCRApi {
    val retrofitOCRService : OCRApiService by lazy {
        retrofitForOCR.create(OCRApiService::class.java) }
}
