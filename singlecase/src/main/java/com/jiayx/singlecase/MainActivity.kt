package com.jiayx.singlecase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jiayx.singlecase.databinding.ActivityMainBinding
import com.jiayx.singlecase.single.SingletonObject05

class MainActivity : AppCompatActivity() {
    val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.text.setOnClickListener{
            Log.d("singleton_log", "对象: ${SingletonObject05.singletonObject05()}")
        }
    }
}