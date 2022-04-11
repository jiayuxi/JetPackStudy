package com.jiayx.coroutine.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jiayx.coroutine.databinding.ActivityCoroutineScopeBinding
import kotlinx.coroutines.delay

/**
 *Created by yuxi_
on 2022/4/11
CoroutineScope lifecycleScope 绑定声明周期，页面销毁，自动取消协程
 */
class CoroutineScopeActivity : AppCompatActivity() {
    private val binding: ActivityCoroutineScopeBinding by lazy {
        ActivityCoroutineScopeBinding.inflate(layoutInflater)
    }
    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        lifecycleScope.launchWhenCreated {
            while (count < 100) {
                count++
                Log.d("practices_log", "lifecycleScope: $count")
                delay(1000)
            }
        }
    }
}