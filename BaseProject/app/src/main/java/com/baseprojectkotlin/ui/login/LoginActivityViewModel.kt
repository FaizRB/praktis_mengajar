package com.baseprojectkotlin.ui.login

import com.baseprojectkotlin.utils.BaseViewModel

class LoginActivityViewModel: BaseViewModel() {

    companion object {
        const val MASUK = "MASUK"
    }

    fun masuk() {
        action.value = MASUK
    }
}