package com.baseprojectkotlin.api.rest

import com.google.gson.JsonObject
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface UserServices {
    @POST("predi")
    suspend fun getUserList(): NetworkResponse<JsonObject, ErrorResponse>

    @GET("newtoken")
    suspend fun getNewToken(): TokenResponse
}