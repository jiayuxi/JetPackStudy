package com.jiayx.singlecase.single

import android.util.Log

/**
 *Created by yuxi_
on 2022/3/27
枚举单例模式
 */
enum class SingletonObjectKt06 {
    INSTANCE;

    fun show() {
        println("show: kotlin 枚举单例模式")
    }
}