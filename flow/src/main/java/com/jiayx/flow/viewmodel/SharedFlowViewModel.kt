package com.jiayx.flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiayx.flow.bean.Event
import com.jiayx.flow.utils.LocalEventBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/1
 */
class SharedFlowViewModel : ViewModel() {

    private lateinit var job: Job

    fun startRefresh() {
        job = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                LocalEventBus.postEvent(Event(System.currentTimeMillis()))
            }
        }
    }

    fun stopRefresh() {
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}