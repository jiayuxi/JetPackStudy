package com.jiayx.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.jiayx.timer.databinding.ActivityMainBinding
import com.jiayx.timer.tool.TimeTask
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var timerTask : TimeTask<TimeTask.Task>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        timerTask = TimeTask(this,"abc", object : TimeTask.Task {

            override fun period(): Long {
                return 6000
            }

            override fun exeTask() {
                Log.d("timeTask_log", "exeTask: 定时任务 ")
            }
        })
        timerTask?.startLooperTask()
        // delay为0表示没有延迟，立即执行一次task
        Timer().schedule(object : TimerTask(){
            override fun run() {
                Log.d("timeTask_log", "立即执行任务")
            }
        },0)
        //延迟1秒，执行一次task
        Timer().schedule(object : TimerTask(){
            override fun run() {
                Log.d("timeTask_log", "延迟一秒执行任务")
            }
        },1000)
        // 立即执行一次task，然后每隔2秒执行一次task
        Timer().schedule(object : TimerTask(){
            override fun run() {
                Log.d("timeTask_log", "循环任务")
            }

            override fun cancel(): Boolean {
                return super.cancel()
            }

            override fun scheduledExecutionTime(): Long {
                return super.scheduledExecutionTime()
            }
        },0,2000)

    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask?.stopLooperTask()
        timerTask?.onClose()
    }
}