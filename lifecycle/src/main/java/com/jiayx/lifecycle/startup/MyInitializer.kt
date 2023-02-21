package com.jiayx.lifecycle.startup

import android.content.Context
import android.os.StrictMode
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.startup.Initializer
import com.jiayx.lifecycle.BuildConfig
import com.jiayx.lifecycle.application.ApplicationObserver

/**
 *Created by yuxi_
on 2022/4/26
 */
class MyInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        // 监听程序的声明周期
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationObserver())
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build()
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}