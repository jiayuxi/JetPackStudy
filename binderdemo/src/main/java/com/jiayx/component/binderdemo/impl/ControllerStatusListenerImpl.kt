package com.jiayx.component.binderdemo.impl

import com.jiayx.component.binderdemo.IControllerStatusListener

/**
 *  author : Jia yu xi
 *  date : 2023/3/7 0:18:18
 *  description :
 */
abstract class ControllerStatusListenerImpl : IControllerStatusListener.Stub() {
    override fun onPauseSuccess() {

    }

    override fun onPauseFailed(errorCode: Int) {
    }

    override fun onPlaySuccess() {
    }

    override fun onPlayFailed(errorCode: Int) {
    }
}