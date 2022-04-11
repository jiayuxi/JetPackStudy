package com.jiayx.coroutine.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.coroutine.databinding.ActivityGlobalScopeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/11
GlobalScope 启动的协程。生命周期是全局的
 */
class GlobalScopeActivity : AppCompatActivity() {
    private val binding: ActivityGlobalScopeBinding by lazy {
        ActivityGlobalScopeBinding.inflate(layoutInflater)
    }
    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        GlobalScope.launch {
            while (count < 100) {
                count++
                Log.d("practices_log", "GlobalScope: $count")
                delay(1000)
            }
        }
    }
}