package com.jiayx.component.annotation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.component.annotation.view.InjectUtils
import com.jiayx.component.annotation.view.OnClick
import com.jiayx.component.annotation.view.OnLongClick

const val TAG: String = "annotation_event"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InjectUtils.injectEvent(this)
    }

    @OnClick(*[R.id.button1, R.id.button2])
    fun click(view: View) {
        when (view.id) {
            R.id.button1 -> Log.d(TAG, "click: 按钮1")
            R.id.button2 -> Log.d(TAG, "click: 按钮2")
        }
    }


    @OnLongClick(*[R.id.button1, R.id.button2])
    fun longClick(view: View): Boolean {
        when (view.id) {
            R.id.button1 -> Log.d(TAG, "longClick: 按钮1")
            R.id.button2 -> Log.d(TAG, "longClick: 按钮2")
        }
        return false
    }
}