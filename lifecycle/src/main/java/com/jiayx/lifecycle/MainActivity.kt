package com.jiayx.lifecycle

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.Looper
import android.widget.Chronometer
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.lifecycle.*
import com.jiayx.lifecycle.databinding.ActivityMainBinding
import com.jiayx.lifecycle.service.MyService
import com.jiayx.lifecycle.viewModel.MyViewModel

/**
 * LifecycleOwner
 */
class MainActivity : AppCompatActivity(), LifecycleOwner {
    private val viewModel: MyViewModel by lazy {
        ViewModelProvider(this)[MyViewModel::class.java]
    }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_JetpackStudy)
        super.onCreate(savedInstanceState)
        //异步加载布局
       /* AsyncLayoutInflater(this).inflate(
            R.layout.activity_main,
            null
        ) { view, _, _ ->
            setContentView(view)
        }*/
        setContentView(binding.root)
        lifecycle.addObserver(binding.myChronoMeter)
        lifecycle.addObserver(MyObserver(lifecycle))
        lifecycle.addObserver(viewModel)
        initAction()
        // idHandler 空闲加载 数据
//        Looper.myQueue().addIdleHandler {
//            Thread.sleep(3000)
//            false
//        }
    }

    private fun initAction() {
        binding.button.setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }
        binding.button2.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(binding.myChronoMeter)
        lifecycle.removeObserver(MyObserver(lifecycle))
        lifecycle.removeObserver(viewModel)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Debug.stopMethodTracing()
    }
}