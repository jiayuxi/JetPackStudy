package com.jiayx.flow.bean

import kotlinx.coroutines.CompletableDeferred

/**
 *Created by yuxi_
on 2022/4/3
 */
sealed class CounterMsg{
    object IncCounter : CounterMsg() // 递增计数器的单向消息
    class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求
}
