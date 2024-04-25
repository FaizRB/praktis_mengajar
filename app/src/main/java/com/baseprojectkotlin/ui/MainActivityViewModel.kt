package com.baseprojectkotlin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.baseprojectkotlin.utils.BaseViewModel
import com.baseprojectkotlin.model.User
import com.baseprojectkotlin.BuildConfig
import com.baseprojectkotlin.repository.UserRepository
import com.baseprojectkotlin.utils.DefaultConsUtils.FLAVOR
import com.baseprojectkotlin.utils.DefaultConsUtils.gson
import com.baseprojectkotlin.utils.TokenUtils
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: UserRepository) : BaseViewModel() {
    val dataUser = MutableLiveData<User>().apply { value = User(title = "Halo Bosku") }

    companion object {
        const val LOAD_DATA = "LOAD_DATA"
    }

    init {

    }

    fun loadUser() {
        action.value = LOAD_DATA
    }

    fun loadData() {
        isLoad.value = true
        viewModelScope.launch {
            when(val result = repository.listUser()) {
                is NetworkResponse.Success -> {
                    try {
                        val item = gson.fromJson(result.body, User::class.java)
                        dataUser.value = item
                    } catch (e: Exception){
                        e.printStackTrace()
                    }

                    isLoad.value = false
                }
                is NetworkResponse.ServerError -> {
                    if (BuildConfig.FLAVOR == FLAVOR) println("SERVER ERROR ${result.body}")
                }
                is NetworkResponse.NetworkError -> {
                    if (BuildConfig.FLAVOR == FLAVOR) println("NETWORK ERROR ${result.error}")
                }
            }
        }
    }
}