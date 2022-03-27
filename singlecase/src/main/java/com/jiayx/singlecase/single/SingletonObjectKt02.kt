package com.jiayx.singlecase.single

import android.util.Log

/**
 *Created by yuxi_
on 2022/3/27
懒汉式 单例模式
 */
class SingletonObjectKt02 {

    companion object {
        private var instance: SingletonObjectKt02? = null
            get() {
                if (field == null) {
                    field = SingletonObjectKt02()
                }
                return field
            }

        fun get(): SingletonObjectKt02? {
            return instance
        }
    }

    fun show() {
        println("show: kotlin 懒汉式单例模式")
    }
}