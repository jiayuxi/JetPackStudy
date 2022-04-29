package com.jiayx.flow.utils

import com.jiayx.flow.bean.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 *Created by yuxi_
on 2022/4/1
 */
object LocalEventBus {
    // MutableSharedFlow 可变
    //        events.tryEmit() 尝试发送数据
//        events.subscriptionCount 共享数据流的订阅者数量
    val events = MutableSharedFlow<Event>()
    suspend fun postEvent(event: Event) {
        events.emit(event)

    }
}