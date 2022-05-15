package com.jiayx.livedata

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jiayx.livedata.bean.DefaultDataSource
import com.jiayx.livedata.databinding.ActivityLivedataBinding
import com.jiayx.livedata.viewmodel.LiveDataViewModel
import kotlinx.coroutines.Dispatchers

/**
 *Created by yuxi_
on 2022/5/14
 */
class LiveDataActivity : AppCompatActivity() {
    private val viewModel: LiveDataViewModel by lazy {
        LiveDataViewModel(DefaultDataSource(Dispatchers.IO))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLivedataBinding>(
            this,
            R.layout.activity_livedata
        )
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.currentWeather.observe(this, Observer {
            Log.d("liveData_log", "$it")
        })
    }
}