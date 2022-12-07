package com.jiayx.component.mvi.model

/**
 *  author : Jia yu xi
 *  date : 2022/12/7 22:19:19
 *  description :
 */
sealed class LoginAction {
    data class UpdateUserName(val userName: String) : LoginAction()
    data class UpdatePassword(val password: String) : LoginAction()
    object Login : LoginAction()
}
