package com.jiayx.component.dispatchevent.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.LinearLayoutCompat
import com.jiayx.component.dispatchevent.utils.TAG

/**
 *  author : Jia yu xi
 *  date : 2023/3/1 17:05:05
 *  description :
 */
class CustomLinearLayout(context: Context, attrs: AttributeSet) :
    LinearLayoutCompat(context, attrs) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "GroupView onTouchEvent: ")
        }
        // TODO return false 或 super 事件从下 往上 调用 父控件的 onTouchEvent
        // TODO  return true 消费事件，事件终止
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "GroupView dispatchTouchEvent: ")
        }
        //Todo return 返回 false，ViewGroup 事件分发 终止，调用父控件的 onTouchEvent
        // TODO return 返回 true ，ViewGroup 事件分发终止，自行消费
        // TODO return 返回 super 事件分发给自己的类里的方法 onInterceptTouchEvent
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "GroupView onInterceptTouchEvent: ")
        }
        // todo return  true，事件拦截，交给自己 onTouchEvent 方法进行处理，如果 onTouchEvent 返回 true ，
        //  自行消费事件，如果 返回 false 或 super ， 事件从下 往上 调用 父控件的 onTouchEvent
        // todo return false 或 super 不拦截事件，事件继续往子view的 dispatchTouchEvent 传递
        return super.onInterceptTouchEvent(ev)
    }
}