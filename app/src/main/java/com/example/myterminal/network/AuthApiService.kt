package com.example.myterminal.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

//TODO: find service's url
var AUTH_SERVICE_URL = ""

private val moshiAuth = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitForAuth = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiAuth))
    .baseUrl(AUTH_SERVICE_URL)
    .build()

interface AuthApiService {
    //TODO: path for post data
    @POST("")
    suspend fun postPassportData(@Body passportDataBody: Map<String, Any>): Call<ResponseBody>
}

object AuthApi {
    val retrofitAuthService : AuthApiService by lazy {
        retrofitForAuth.create(AuthApiService::class.java) }
}

