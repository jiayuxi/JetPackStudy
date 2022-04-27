package com.jiayx.lifecycle

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import androidx.compose.runtime.internal.LiveLiteralFileInfo
import androidx.lifecycle.*

/**
 *Created by yuxi_
on 2022/4/25
 */
class MyChronoMeter(context: Context?, attrs: AttributeSet?) : Chronometer(context, attrs),
    DefaultLifecycleObserver {


    private var elapsedTime = 0L
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        base = SystemClock.elapsedRealtime() - elapsedTime
        start()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        elapsedTime = SystemClock.elapsedRealtime() - base
        stop()
    }

}