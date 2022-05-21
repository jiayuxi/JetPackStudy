package com.jiayx.coroutine.activity

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 *Created by yuxi_
on 2022/5/20
 */
class ExceptionHandler : CoroutineExceptionHandler {

    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("exception_log", "Unhandled coroutine Exception : $exception")
    }
}