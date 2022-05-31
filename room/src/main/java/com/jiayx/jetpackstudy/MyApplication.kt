package com.jiayx.jetpackstudy

import android.app.Application
import com.jiayx.jetpackstudy.ui.main.utils.AppHelper
import dagger.hilt.android.HiltAndroidApp

/**
 *Created by yuxi_
on 2022/3/9
 */
@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppHelper.init(this)
    }
}