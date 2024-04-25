package com.baseprojectkotlin.utils

import com.baseprojectkotlin.BuildConfig
import com.baseprojectkotlin.api.rest.UserServices
import com.baseprojectkotlin.utils.DefaultConsUtils.gson
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor: Interceptor {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()

    private val userService: UserServices = retrofit.create(UserServices::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenUtils.getToken()
        val request = chain.request()
        if (token.isNotEmpty()) {
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method, original.body)
                .addHeader("Cache-control", "no-cache")
                .addHeader("Authorization", "Bearer " + TokenUtils.getToken())
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build()
            val response = chain.proceed(request)
            if (response.code == 401 || response.code == 422) {
                val refTok = runBlocking {
                    val resp = userService.getNewToken()
                    TokenUtils.setToken(resp.token)
                    resp.token
                }

                val newrequest = original.newBuilder()
                    .method(original.method, original.body)
                    .addHeader("Cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer $refTok")
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
                return chain.proceed(newrequest)
            } else{
                return response
            }
        } else {
            val refTok = runBlocking {
                val resp = userService.getNewToken()
                TokenUtils.setToken(resp.token)
                resp.token
            }

            val newrequest = request.newBuilder()
                .method(request.method, request.body)
                .addHeader("Cache-control", "no-cache")
                .addHeader("Authorization", "Bearer $refTok")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build()
            return chain.proceed(newrequest)
        }
    }
}