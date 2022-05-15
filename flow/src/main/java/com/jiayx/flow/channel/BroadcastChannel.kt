package com.jiayx.flow.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.broadcast

/**
 *Created by yuxi_
on 2022/5/14
todo 如果生产者和消费者的生命周期不同或者彼此完全独立运行时，请使用 BroadcastChannel。
如果您希望生产者有独立的生命周期，同时向任何存在的监听者发送当前数据的时候，
BroadcastChannel API 非常适合这种场景。在这种情况下，当新的监听者开始消费事件时，生产者不需要每次都被执行
 */

fun main() {
    broadcastChannel()
}

private fun `broadcastChannel`() = runBlocking {
//    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
    // channel 转化为 broadcastChannel
    val channel = Channel<Int>()
    val broadcastChannel = channel.broadcast()
    val produce = GlobalScope.launch {
        List(3) {
            delay(100)
            broadcastChannel.send(it)
        }
        //关闭
        broadcastChannel.close()
    }
    List(3) { index ->
        GlobalScope.launch {
            //注册
            val receiveChannel = broadcastChannel.openSubscription()
            for (i in receiveChannel) {
                println("[#${index}] received: $i")
            }
        }
    }.joinAll()
}
