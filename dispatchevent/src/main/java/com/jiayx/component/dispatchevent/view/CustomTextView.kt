package com.jiayx.component.dispatchevent.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.jiayx.component.dispatchevent.utils.TAG

/**
 *  author : Jia yu xi
 *  date : 2023/3/1 17:01:01
 *  description :
 */

class CustomTextView constructor(context: Context, attrs: AttributeSet) :
    AppCompatTextView(context, attrs) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "View dispatchTouchEvent: ")
        }
        // TODO return true 终结事件 view自行消费 ，
        // todo return false 是回溯父类的 onTouchEvent 事件
        // todo return super 默认时实现 ，调用view自己 的 onTouchEvent 事件
        return super.dispatchTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "View onTouchEvent: ")
        }
        // TODO return true 自行消费事件
        // TODO return false 或 super 回溯给父类控件 的 onTouchEvent
        return true
    }
}