package com.jiayx.livedata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jiayx.livedata.databinding.ActivityMainBinding
import com.jiayx.livedata.event.LiveDataBus
import com.jiayx.livedata.event.LiveDataBusData

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val eventBus by lazy { MutableLiveData<Int>() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        LiveDataBus.instance.with("key_mainActivity", String::class.java)
            .observe(this, Observer {
                Toast.makeText(this, "当前页面：$it", Toast.LENGTH_SHORT).show()
            })
        // 打开新的 activity
        binding.button.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }
        //向没有启动activity 发送消息
        binding.button1.setOnClickListener {
            LiveDataBus.instance.post("key_mainActivity2", "message from activity A")
        }
        // 发送本地消息
        binding.button2.setOnClickListener {
            LiveDataBus.instance.post("key_mainActivity", "Hello livedata 学习")
            eventBus.value = 1
        }

        eventBus.observe(this, Observer {
            Log.d("liveData_log", " observer 1 : $it")
        })
        eventBus.observe(this, Observer {
            Log.d("liveData_log", " observer 2 : $it")
            if (it == 1) {
                eventBus.value = 2
            }
        })
        eventBus.observe(this, Observer {
            Log.d("liveData_log", " observer 3 : $it")
        })
        eventBus.observe(this, Observer {
            Log.d("liveData_log", " observer 4 : $it")
        })
        eventBus.observe(this, Observer {
            Log.d("liveData_log", " observer 5 : $it")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.instance.remove("key_mainActivity")
    }
}