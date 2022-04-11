package com.jiayx.coroutine.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.coroutine.databinding.ActivityRxjavaRetrofitBinding

/**
 *Created by yuxi_
on 2022/4/11
 */
class RxjavaRetrofitActivity : AppCompatActivity() {
    val binding: ActivityRxjavaRetrofitBinding by lazy {
        ActivityRxjavaRetrofitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}