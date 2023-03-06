package com.jiayx.component.dispatchevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.jiayx.component.dispatchevent.utils.TAG

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        textView.setOnClickListener {
            Log.d(TAG, "onCreate: textView onClick")
        }
        textView.setOnTouchListener { v, event ->
            Log.d(TAG, "onCreate: textView OnTouchListener")  
            false
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action == MotionEvent.ACTION_DOWN){
            Log.d(TAG, "Activity dispatchTouchEvent: ")
        }
        //todo return true 或 false 自己消费，事件不在分发
        //todo return super 事件往下分发
        return super.dispatchTouchEvent(ev)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Activity onTouchEvent: ")
        }
        return super.onTouchEvent(event)
    }
}