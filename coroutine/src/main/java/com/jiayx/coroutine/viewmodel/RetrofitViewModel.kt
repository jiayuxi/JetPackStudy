package com.jiayx.coroutine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiayx.coroutine.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait

/**
 *Created by yuxi_
on 2022/4/11
 */
class RetrofitViewModel : ViewModel() {
    private val repository by lazy { Repository() }

    private val stateFlow = MutableStateFlow<String?>(null)
    val resultFlow = stateFlow.asStateFlow()

    /**
     * 协程网络请求
     */
    fun coroutineRetrofit() {
        viewModelScope.launch {
            val job = async {
                repository.loadUsersKt("rengwuxian")
            }
            val job2 = async {
                repository.loadUsersKt("google")
            }
            stateFlow.emit("${job.await()[0].name}" + "${job2.await()[0].name}")
        }
    }
}