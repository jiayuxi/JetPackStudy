package com.jiayx.gesturedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GestureDetectorCompat
import com.jiayx.gesturedetector.databinding.ActivityMainBinding
import com.jiayx.gesturedetector.listener.MyDoubleTapImpl
import com.jiayx.gesturedetector.listener.MyGestureDetectorImpl

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val gestureDetector: GestureDetectorCompat by lazy {
        val temp = GestureDetectorCompat(this, MyGestureDetectorImpl())
        temp.setOnDoubleTapListener(MyDoubleTapImpl())
        return@lazy temp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.text.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }

}