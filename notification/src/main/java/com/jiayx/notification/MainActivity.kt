package com.jiayx.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiayx.notification.databinding.ActivityMainBinding
import com.juxing.helment.x20.lib_wifi.wifi.WiFiManager

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction(){
        //创建基本通知
        binding.button1.setOnClickListener {

       }
    }
}