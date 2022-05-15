package com.jiayx.livedata.viewmodel

import androidx.lifecycle.*
import com.jiayx.livedata.bean.DataSource
import com.jiayx.livedata.bean.DefaultDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 *Created by yuxi_
on 2022/5/14
 */
// 将协程与 LiveData 一起使用
class LiveDataViewModel(private val dataSource: DataSource) : ViewModel() {
    //switchMap 是数据变换中的一种，它订阅了 userId 的变化，并且其代码体会在感知到 userId 变化时执行。
    private val userId: LiveData<String> = MutableLiveData()
    val user = userId.switchMap { id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(id)
        }
    }
    val currentTime = dataSource.getCurrentTime()

    val currentTimeTransformed = currentTime.switchMap {
        liveData { emit(timeStampToTime(it)) }
    }

    // Simulates a long-running computation in a background thread
    private suspend fun timeStampToTime(timestamp: Long): String {
        delay(500)  // Simulate long operation
        val date = Date(timestamp)
        return date.toString()
    }

    // Exposed liveData that emits and single value and subsequent values from another source.
    val currentWeather: LiveData<String> = liveData {
        emit(LOADING_STRING)
        val emitSource = emitSource(dataSource.fetchWeather())
//        delay(3000)
//        emitSource.dispose()
    }

    // Exposed cached value in the data source that can be updated later on
    val cachedValue = dataSource.cachedData

    // Called when the user clicks on the "FETCH NEW DATA" button. Updates value in data source.
    fun onRefresh() {
        // Launch a coroutine that reads from a remote data source and updates cache
        viewModelScope.launch {
            dataSource.fetchNewData()
        }
    }

    companion object {
        // Real apps would use a wrapper on the result type to handle this.
        const val LOADING_STRING = "Loading..."
    }
}

