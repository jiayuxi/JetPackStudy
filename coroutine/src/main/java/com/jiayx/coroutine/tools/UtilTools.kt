package com.jiayx.coroutine

import android.content.Context
import android.content.Intent

/**
 * 跳转startActivity
 */
fun <T> startToActivity(context: Context, cls: Class<T>) {
    context?.apply {
        startActivity(Intent(this, cls))
    }
}