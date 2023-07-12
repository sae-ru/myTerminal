package com.example.myterminal.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

//TODO: find service's url
var AUTH_SERVICE_URL = "192.168.143.1"

//private val okHttpClientAuth = OkHttpClient.Builder().build()

private val moshiAuth = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitForAuth = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiAuth))
    .baseUrl("http://$AUTH_SERVICE_URL/")
//    .client(okHttpClientAuth)
    .build()

interface AuthApiService {

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth")
    suspend fun postAuthorization(@Body authorizationBody: AuthTokenRequest): AuthTokenResponse

    @Headers("Content-Type: application/json")
    @POST("api/v1/person")
    suspend fun postPassportData(@Body passportDataBody: AuthPersonRequest, @Query("token") token: String): AuthPersonResponse
}

object AuthApi {
    val retrofitAuthService: AuthApiService by lazy {
        retrofitForAuth.create(AuthApiService::class.java)
    }
}

