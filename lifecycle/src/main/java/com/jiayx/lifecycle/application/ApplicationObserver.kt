package com.jiayx.lifecycle.application

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.log

/**
 *Created by yuxi_
on 2022/4/26
ProcessLifecycleOwner是针对整个应用程序的监听，与Activity数量无关，你有一个Activity或多个Activity，对ProcessLifecycleOwner来说是没有区别的。
Lifecycle.Event.ON_CREATE只会被调用一次，而Lifecycle.Event.ON_DESTROY永远不会被调用。
当应用程序从后台回到前台，或者应用程序被首次打开时，会依次调用Lifecycle.Event.ON_START和Lifecycle.Event.ON_RESUME。
当应用程序从前台退到后台（用户按下Home键或任务菜单键），会依次调用Lifecycle.Event.ON_PAUSE和Lifecycle.Event.ON_STOP。
 */
const val TAG = "jia_lifecycle"
class ApplicationObserver : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(TAG, "onCreate: ")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d(TAG, "onStart: ")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(TAG, "onResume: ")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(TAG, "onPause: ")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d(TAG, "onStop: ")
    }
    // TODO 永远不会调用
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(TAG, "onDestroy: ")
    }
}