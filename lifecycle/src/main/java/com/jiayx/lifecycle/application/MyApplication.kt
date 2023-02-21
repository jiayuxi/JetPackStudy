package com.jiayx.lifecycle.application

import android.app.Application
import android.os.Debug
import android.os.Environment
import java.io.File

/**
 *Created by yuxi_
on 2022/4/26
 */
class MyApplication : Application() {
    init{
        // 跟踪分析
//        Debug.startMethodTracing("enjoy")
        // 采样
//        Debug.startMethodTracingSampling(File(Environment.getExternalStorageDirectory(),"enjoy").absolutePath,
//        8 * 1024 * 1024,1000)
    }
}