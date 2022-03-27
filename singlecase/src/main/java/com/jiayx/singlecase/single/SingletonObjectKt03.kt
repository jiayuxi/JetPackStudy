package com.jiayx.singlecase.single

import android.util.Log

/**
 *Created by yuxi_
on 2022/3/27
线程安全的懒汉式单例
 */
class SingletonObjectKt03 {
    companion object {
        private var instance: SingletonObjectKt03? = null
            get() {
                if (field == null) {
                    field = SingletonObjectKt03()
                }
                return field
            }

        @Synchronized
        fun get(): SingletonObjectKt03? {
            return instance
        }
    }

    fun show() {
        println("show: kotlin 线程安全的饿汉式单例模式")
    }
}