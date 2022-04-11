package com.jiayx.coroutine.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jiayx.coroutine.databinding.ActivityCoroutineRetrofitBinding
import com.jiayx.coroutine.viewmodel.RetrofitViewModel
import kotlinx.coroutines.flow.collect

/**
 *Created by yuxi_
on 2022/4/11
协程 + retrofit 网络请求
 */
class CoroutineRetrofitActivity : AppCompatActivity() {
    private val binding: ActivityCoroutineRetrofitBinding by lazy {
        ActivityCoroutineRetrofitBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<RetrofitViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        viewModel.coroutineRetrofit()
        lifecycleScope.launchWhenCreated {
            viewModel.resultFlow.collect {
                binding.activityCoroutineRetrofitText.text = it
            }
        }
    }
}