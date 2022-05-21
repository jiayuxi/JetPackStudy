package com.jiayx.gesturedetector

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.jiayx.gesturedetector.databinding.ActivityMainBinding
import com.jiayx.gesturedetector.listener.MySimpleOnGestureImpl

/**
 *Created by yuxi_
on 2022/5/21
 */
class SimpleOnGestureActivity : AppCompatActivity() ,View.OnTouchListener{
    private val simpleOnGestureImpl:GestureDetectorCompat by lazy {
        GestureDetectorCompat(this,MySimpleOnGestureImpl())
    }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.text.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
       return simpleOnGestureImpl.onTouchEvent(event)
    }
}