package com.baseprojectkotlin

import android.app.Application
import com.baseprojectkotlin.api.rest.UserServices
import com.baseprojectkotlin.repository.UserRepository
import com.baseprojectkotlin.ui.MainActivityViewModel
import com.baseprojectkotlin.ui.login.LoginActivityViewModel
import com.baseprojectkotlin.utils.DefaultConsUtils.REST_SERVICE
import com.baseprojectkotlin.utils.TokenUtils
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.orhanobut.hawk.Hawk
import com.baseprojectkotlin.utils.AuthInterceptor
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    repositoryModule,
                    apiModule,
                    viewModelModule
                )
            )
        }
    }

    private val repositoryModule = module {
        single { UserRepository(get(named(REST_SERVICE))) }
    }

    private val apiModule = module {
        single(named(REST_SERVICE)) { createRestService<UserServices>(createOkHttpClient()) }
    }

    private val viewModelModule = module {
        factory { MainActivityViewModel(get()) }
        factory { LoginActivityViewModel() }
    }

    private fun createOkHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method, original.body)
                .addHeader("Cache-control", "no-cache")
                .addHeader("Authorization", "Bearer " + TokenUtils.getToken())
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build()

            chain.proceed(request)
        }

        val interceptor = AuthInterceptor()
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .cache(null)
            .addInterceptor(interceptor)
            .build()
    }

    private inline fun <reified T> createRestService(okHttpClient: OkHttpClient): T {
        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()

        return retrofit.create(T::class.java)
    }
}
