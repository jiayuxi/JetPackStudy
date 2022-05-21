package com.jiayx.gesturedetector.listener

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent

/**
 *Created by yuxi_
on 2022/5/21
 */
class MyDoubleTapImpl : GestureDetector.OnDoubleTapListener {
    companion object{
        const val TAG = "jia_gesture"
    }
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.d(TAG, "DoubleDetector onSingleTapConfirmed: ")
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.d(TAG, "DoubleDetector onDoubleTap: ${e.toString()} ")
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        e?.let {
            if(it.action == MotionEvent.ACTION_UP){
                Log.d(TAG, "DoubleDetector onDoubleTapEvent: ")
            }
        }
        return true
    }
}