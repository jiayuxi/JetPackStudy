package com.jiayx.gesturedetector.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.gesturedetector.databinding.ActivityStartBinding

/**
 *Created by yuxi_
on 2022/5/21
 */
class StartActivity : AppCompatActivity() {
    private val binding:ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
             startActivity(Intent(this, MainActivity::class.java))
        }
        binding.button2.setOnClickListener {
            startActivity(Intent(this, SimpleOnGestureActivity::class.java))
        }
        binding.button3.setOnClickListener {
            startActivity(Intent(this, GestureVideoActivity::class.java))
        }
    }
}