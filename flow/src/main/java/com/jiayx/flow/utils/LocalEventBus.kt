package com.jiayx.flow.utils

import com.jiayx.flow.bean.Event
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 *Created by yuxi_
on 2022/4/1
 */
object LocalEventBus {
    val events = MutableSharedFlow<Event>()
    suspend fun postEvent(event: Event) {
        events.emit(event)
    }
}