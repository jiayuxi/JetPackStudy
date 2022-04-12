package com.jiayx.coroutine.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jiayx.coroutine.databinding.ActivityRxjavaRetrofitBinding
import com.jiayx.coroutine.viewmodel.RetrofitViewModel

/**
 *Created by yuxi_
on 2022/4/11
 */
class RxjavaRetrofitActivity : AppCompatActivity() {
    private val binding: ActivityRxjavaRetrofitBinding by lazy {
        ActivityRxjavaRetrofitBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<RetrofitViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        viewModel.rxjavaRetrofit()
        lifecycleScope.launchWhenCreated {
            viewModel.resultFlow.collect {
                binding.activityRxjavaRetrofitText.text = it
            }
        }
    }
}