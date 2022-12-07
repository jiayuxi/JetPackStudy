package com.jiayx.component.mvi.model

import android.util.Log

/**
 *  author : Jia yu xi
 *  date : 2022/12/7 22:12:12
 *  description :
 */
sealed class LoginViewEvent {
    data class ShowToast(val message: String) : LoginViewEvent()
    object ShowLoadingDialog : LoginViewEvent()
    object DismissLoadingDialog : LoginViewEvent()
}
