package com.jiayx.livedata

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jiayx.livedata.databinding.Activity2MainBinding
import com.jiayx.livedata.event.LiveDataBus
import com.jiayx.livedata.event.LiveDataBusData
import kotlin.concurrent.thread

/**
 *Created by yuxi_
on 2022/5/1
 */
class Main2Activity : AppCompatActivity() {
    private val binding: Activity2MainBinding by lazy {
        Activity2MainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        LiveDataBus.instance.with("key_mainActivity2", String::class.java)
            .observe(this, Observer {
                Toast.makeText(this, "本地消息：$it", Toast.LENGTH_SHORT).show()
            })
        // 本地消息发送
        binding.button3.setOnClickListener {
            LiveDataBus.instance.post("key_mainActivity2", "发送本地消息")
        }
        // 子线程发送消息
        binding.button4.setOnClickListener {
            thread {
                Thread.sleep(5000)
                LiveDataBus.instance.post("key_mainActivity2", "线程中发送消息")
            }
        }
        // 跨页面发送消息
        binding.button5.setOnClickListener {
            LiveDataBus.instance.post("key_mainActivity", "跨页面传递消息")
        }
        // 事件泄露
        binding.button6.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.instance.remove("key_mainActivity2")
    }
}