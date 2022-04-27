package com.jiayx.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.jiayx.lifecycle.application.TAG

/**
 *Created by yuxi_
on 2022/4/26
 */
class MyObserver : DefaultLifecycleObserver {
    private var lifecycle: Lifecycle? = null

    constructor(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) == true) {
            Log.d(TAG, "onStart: 开始运行操作")
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d(TAG, "onStop: 停止运行")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }
}