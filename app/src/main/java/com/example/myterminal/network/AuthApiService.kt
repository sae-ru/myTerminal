package com.example.myterminal.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

private val moshiAuth = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//TODO: find service's url
var AUTH_SERVICE_URL: String = "192.168.143.1"
    set(value) {
        field = value
        recreateNetworking()
    }

private var retrofitForAuth: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiAuth))
    .baseUrl("http://$AUTH_SERVICE_URL/")
    .build()

private fun recreateNetworking() {
    retrofitForAuth = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshiAuth))
        .baseUrl("http://$AUTH_SERVICE_URL/")
        .build()
    AuthApi.retrofitAuthService = retrofitForAuth.create(AuthApiService::class.java)
}

interface AuthApiService {

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth")
    suspend fun postAuthorization(@Body authorizationBody: AuthTokenRequest): AuthTokenResponse

    @Headers(
        "Content-Type: application/json",
        "accept: application/json"
    )
    @POST("api/v1/person/")
    suspend fun postPassportData(@Body passportDataBody: AuthPersonRequest, @Header("Authorization") token: String): AuthPersonResponse
}

object AuthApi {
    var retrofitAuthService: AuthApiService = retrofitForAuth.create(AuthApiService::class.java)
}

