package com.baseprojectkotlin.utils

import com.orhanobut.hawk.Hawk

object TokenUtils {
    private const val TOKEN = "TOKEN"

    fun setToken(data: String) {
        Hawk.put(TOKEN, data)
    }

    fun getToken(): String {
        return Hawk.get(TOKEN, "")
    }
}