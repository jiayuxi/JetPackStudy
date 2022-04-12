package com.jiayx.coroutine.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jiayx.coroutine.databinding.ActivityRetrofitBinding
import com.jiayx.coroutine.viewmodel.RetrofitViewModel

/**
 *Created by yuxi_
on 2022/4/11
retrofit 网络请求
 */
class RetrofitActivity : AppCompatActivity() {
    private val binding: ActivityRetrofitBinding by lazy {
        ActivityRetrofitBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<RetrofitViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        lifecycleScope.launchWhenCreated {
            viewModel.resultFlow.collect {
                binding.activityRetrofitText.text = it
            }
        }
        viewModel.retrofit()
    }
}