package com.jiayx.gesturedetector.listener

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent

/**
 *Created by yuxi_
on 2022/5/21
 */
class MyGestureDetectorImpl : GestureDetector.OnGestureListener {
    companion object {
        const val TAG = "jia_gesture"
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.d(TAG, "GestureDetector onDown: ")
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.d(TAG, "GestureDetector onShowPress: ")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d(TAG, "GestureDetector onSingleTapUp: ")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(TAG, "GestureDetector onScroll: ")
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.d(TAG, "GestureDetector onLongPress: ")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(TAG, "GestureDetector onFling: ")
        return true
    }
}