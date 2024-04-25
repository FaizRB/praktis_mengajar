package com.baseprojectkotlin.api.rest

import com.google.gson.annotations.SerializedName

class ErrorResponse(
    @SerializedName("error")
    val error: Error,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)

class TokenResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("token")
    val token: String
)
