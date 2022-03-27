package com.jiayx.singlecase.single

import android.util.Log

/**
 *Created by yuxi_
on 2022/3/27
静态内部类单例模式
 */
class SingletonObjectKt05 {
    companion object {
        val instance = SingletonHelp.help
    }

    private object SingletonHelp {
        val help = SingletonObjectKt05()
    }

    fun show() {
        println("show: kotlin 静态内部类")
    }
}