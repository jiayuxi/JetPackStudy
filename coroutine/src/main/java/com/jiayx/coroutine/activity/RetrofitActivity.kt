package com.jiayx.coroutine.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.coroutine.databinding.ActivityRetrofitBinding

/**
 *Created by yuxi_
on 2022/4/11
retrofit 网络请求
 */
class RetrofitActivity : AppCompatActivity() {
    val binding: ActivityRetrofitBinding by lazy {
        ActivityRetrofitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}