package com.jiayx.component.mvi.model

/**
 *  author : Jia yu xi
 *  date : 2022/12/7 22:09:09
 *  description :
 */
data class LoginViewState(val userName: String = "", val password: String = "") {
    val isLoginEnable: Boolean
        get() = userName.isNotEmpty() && password.length >= 6

    val passwordTipVisible
        get() = password.length in 1..5
}
