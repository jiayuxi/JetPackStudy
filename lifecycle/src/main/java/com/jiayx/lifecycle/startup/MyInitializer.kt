package com.jiayx.lifecycle.startup

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.startup.Initializer
import com.jiayx.lifecycle.application.ApplicationObserver

/**
 *Created by yuxi_
on 2022/4/26
 */
class MyInitializer : Initializer<Unit>{
    override fun create(context: Context) {
        // 监听程序的声明周期
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationObserver())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}