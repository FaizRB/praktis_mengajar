package com.baseprojectkotlin.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.baseprojectkotlin.R
import com.baseprojectkotlin.ui.MainActivity
import com.baseprojectkotlin.ui.login.LoginActivity
import com.baseprojectkotlin.utils.TokenUtils
import kotlinx.coroutines.*
import java.util.Timer

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(!TokenUtils.getToken().isNullOrEmpty()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 2000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }, 2000)
        }
    }
}