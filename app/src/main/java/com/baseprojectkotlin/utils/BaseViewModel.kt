package com.baseprojectkotlin.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    val isLoad = MutableLiveData<Boolean>()
    val action = MutableLiveData<String>()
}