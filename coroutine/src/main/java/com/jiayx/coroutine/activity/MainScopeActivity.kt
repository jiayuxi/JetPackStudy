package com.jiayx.coroutine.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.coroutine.databinding.ActivityMainScopeBinding
import kotlinx.coroutines.*

/**
 *Created by yuxi_
on 2022/4/11
CoroutineScope by MainScope() 绑定activity生命周期
页面销毁 取消协程
 */
class MainScopeActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    val binding: ActivityMainScopeBinding by lazy {
        ActivityMainScopeBinding.inflate(layoutInflater)
    }
    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        launch {
            while (count < 100) {
                count++
                Log.d("practices_log", "MainScope: $count")
                delay(1000)
            }
        }
    }

    override fun onDestroy() {
        //取消协程
        cancel()
        super.onDestroy()
    }
}