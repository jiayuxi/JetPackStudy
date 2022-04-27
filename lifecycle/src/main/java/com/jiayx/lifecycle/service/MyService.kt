package com.jiayx.lifecycle.service

import androidx.lifecycle.LifecycleService
import com.jiayx.lifecycle.MyObserver

/**
 *Created by yuxi_
on 2022/4/27
 */
class MyService : LifecycleService() {

    private val myObserver by lazy {
        MyObserver(lifecycle)
    }

    init {
        lifecycle.addObserver(myObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(myObserver)
    }
}