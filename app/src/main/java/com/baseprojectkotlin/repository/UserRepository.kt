package com.baseprojectkotlin.repository

import com.baseprojectkotlin.api.rest.ErrorResponse
import com.baseprojectkotlin.api.rest.UserServices
import com.google.gson.JsonObject
import com.haroldadmin.cnradapter.NetworkResponse

class UserRepository(private val userServices: UserServices) {
    suspend fun listUser(): NetworkResponse<JsonObject, ErrorResponse> {
        return userServices.getUserList()
    }
}