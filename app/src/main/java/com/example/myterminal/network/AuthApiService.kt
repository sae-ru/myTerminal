package com.example.myterminal.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

//TODO: find service's url
var AUTH_SERVICE_URL = "192.168.143.1"

//private val okHttpClientAuth = OkHttpClient.Builder().build()

private val moshiAuth = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitForAuth = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiAuth))
    .baseUrl("https://$AUTH_SERVICE_URL")
//    .client(okHttpClientAuth)
    .build()

interface AuthApiService {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/auth")
    suspend fun postAuthorizationAsync(@Body authorizationBody: Map<String, String>): AuthResponseToken

    @Headers("Content-Type: application/json")
    @POST("/api/v1/permit")
    suspend fun postPassportData(@Body passportDataBody: Map<String, String>): Call<ResponseBody>
}

object AuthApi {
    val retrofitAuthService: AuthApiService by lazy {
        retrofitForAuth.create(AuthApiService::class.java)
    }
}

