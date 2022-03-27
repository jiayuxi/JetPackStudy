package com.jiayx.singlecase.single

import android.util.Log

/**
 *Created by yuxi_
on 2022/3/27
双重检索 线程安全的饿汉式单例模式
 */
class SingletonObjectKt04 private constructor() {
    companion object {
        val instance: SingletonObjectKt04 by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SingletonObjectKt04()
        }
    }
    
    fun show(){
        println("show: kotlin 双重检索 线程安全的 饿汉式到单例模式")
    }
}