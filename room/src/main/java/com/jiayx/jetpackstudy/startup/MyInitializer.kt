package com.jiayx.jetpackstudy.startup

import android.content.Context
import androidx.startup.Initializer

/**
 *Created by yuxi_
on 2022/3/9
 */
class MyInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        // 初始化 参数
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}