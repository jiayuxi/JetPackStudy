package com.jiayx.flow.viewmodel

import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

/**
 *Created by yuxi_
on 2022/5/14
 */
class FlowViewModel : ViewModel() {
    var result: Int = 0
    var result2: Int = 0
    val oneElementFlow: Flow<Int> = flow {
        // 生产者代码开始执行，流被打开
        emit(1)
        // 生产者代码结束，流将被关闭
    }

    /*
    * Flow 通过协程取消功能提供自动清理功能，因此倾向于执行一些重型任务。
    * 请注意，这里提到的取消是有条件的，一个永不挂起的 Flow 是永不会被取消的:
    * 在我们的例子中，由于delay是一个挂起函数，用于检查取消状态，当订阅者停止监听时，Flow 将会停止并清理资源。
    *
    * */
    // 订阅一次，触发一次发送
    val unlimitedElementFlow: Flow<Int> = flow {
        result = 0
        // 生产者代码开始执行，流被打开
        while (true) {
            result++
            // 执行计算
            emit(result)
            delay(100)
        }
        // 生产者代码结束，流将被关闭
    }

    // 不订阅 不触发 发送
    var twoFlowValue: Flow<Int>? = null
    var job: Job? = null

    init {
        Log.d("flow_unlimited_log", "value : $result ")
        job = viewModelScope.launch(Dispatchers.IO) {
            Log.d("flow_unlimited_log", "value : $result ")
            twoFlowValue = flow {
                while (true) {
                    ensureActive()
                    result2++
                    emit(result2)
                    Log.d("flow_unlimited_log", "value : $result ")
                }
            }
            twoFlowValue?.collectLatest { }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }

    // 取消不起作用
    fun cancelFlow() {
        twoFlowValue?.let {
            Log.d("flow_error", "cancelFlow ")
            it.cancellable()
        } ?: kotlin.run { Log.d("flow_error", "cancelFlow 为空 ") }
    }
}