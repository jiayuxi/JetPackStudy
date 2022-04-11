package com.jiayx.coroutine.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jiayx.coroutine.databinding.ActivityDelayBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/11
挂起函数 与 阻塞函数的 区别
挂起函数：切换到其他线程进行挂起，不会阻塞当前线程
阻塞函数：会阻塞当前线程
 */
class DelayActivity : AppCompatActivity() {
   private val binding: ActivityDelayBinding by lazy {
        ActivityDelayBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        binding.activityDelay1.setOnClickListener {
            Log.d("practices_log", "onCreate: ${Thread.currentThread().name}: start")
            lifecycleScope.launchWhenCreated {
                delay(5000)
                binding.activityDelayText.text = "点击挂起函数"
                Log.d("practices_log", "onCreate: ${Thread.currentThread().name}:after delay")
            }
            Log.d("practices_log", "onCreate: ${Thread.currentThread().name}:end delay")
        }

        binding.activityDelay2.setOnClickListener {
            Log.d("practices_log", "onCreate: ${Thread.currentThread().name}: start")
            Thread.sleep(5000)
            binding.activityDelayText.text = "点击阻塞函数"
            Log.d("practices_log", "onCreate: ${Thread.currentThread().name}:end delay")
        }
    }
}