package com.jiayx.hilt.di

import android.util.Log
import com.jiayx.hilt.init.TAG
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/25
 */
class HiltSimple @Inject constructor() {

    fun doSomething(){
        Log.d(TAG, "doSomething: 调试 Hilt 注入")
    }
}