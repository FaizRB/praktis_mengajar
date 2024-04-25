package com.baseprojectkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.baseprojectkotlin.R
import com.baseprojectkotlin.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        with(binding) {
            lifecycleOwner = this@MainActivity
            vm = viewModel
        }

        initVM()
    }

    private fun initVM() {
        with(viewModel) {
            action.observe(this@MainActivity) {
                when(it) {
                    MainActivityViewModel.LOAD_DATA -> loadData()
                }
            }
        }
    }
}