package com.jiayx.jetpackstudy.ui.main.room

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.jiayx.jetpackstudy.R

/**
 *Created by yuxi_
on 2022/3/8
 */
class RoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_activity)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("touch_event", "onTouchEvent: ${event.toString()}")
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}