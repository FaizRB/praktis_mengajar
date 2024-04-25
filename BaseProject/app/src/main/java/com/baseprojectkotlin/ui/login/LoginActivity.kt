package com.baseprojectkotlin.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.baseprojectkotlin.R
import com.baseprojectkotlin.databinding.ActivityLoginBinding
import com.baseprojectkotlin.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginActivityViewModel by viewModel()
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        with(binding) {
            lifecycleOwner = this@LoginActivity
            vm = viewModel
        }

        initVM()
    }

    private fun initVM() {
        with(viewModel) {
            action.observe(this@LoginActivity) {
                when(it) {
                    LoginActivityViewModel.MASUK -> goToHome()
                }
            }
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}