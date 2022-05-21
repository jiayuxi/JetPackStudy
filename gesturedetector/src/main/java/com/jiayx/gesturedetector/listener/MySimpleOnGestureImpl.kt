package com.jiayx.gesturedetector.listener

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent

/**
 *Created by yuxi_
on 2022/5/21
 */
class MySimpleOnGestureImpl : GestureDetector.SimpleOnGestureListener() {
    companion object{
        const val TAG = "jia_gesture"
    }
    override fun onDown(e: MotionEvent?): Boolean {
        Log.d(TAG, "onDown: ")
        // 返回 true  流程才能正常的执行
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        super.onShowPress(e)
        Log.d(TAG, "onShowPress: ")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapUp: ")
        return super.onSingleTapUp(e)
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(TAG, "onScroll: ")
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    override fun onLongPress(e: MotionEvent?) {
        super.onLongPress(e)
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(TAG, "onFling: ")
        return super.onFling(e1, e2, velocityX, velocityY)
    }

    /**
     * 单击事件 使用这个 更为合适
     * 双击不会调用这个函数
     */
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapConfirmed: ")
        return super.onSingleTapConfirmed(e)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.d(TAG, "onDoubleTap: ${e.toString()}")
        return super.onDoubleTap(e)
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        e?.let {
            if(it.action == MotionEvent.ACTION_UP){
                Log.d(TAG, "onDoubleTapEvent: ")
                return true
            }
        }
        return super.onDoubleTapEvent(e)
    }
}