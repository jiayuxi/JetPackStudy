package com.jiayx.coroutine.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jiayx.coroutine.databinding.ActivityExceptionBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/5/20
 异常捕获
 */
class ExceptionActivity : AppCompatActivity() {
    private val binding: ActivityExceptionBinding by lazy {
        ActivityExceptionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val handler = CoroutineExceptionHandler{_,exception ->
            Log.e("exception_log", "Caught $exception ")
        }

        binding.button.setOnClickListener {
            lifecycleScope.launch{
                Log.d("exception_log", "on Click ")
                "abc".substring(10)
            }
        }
    }
}