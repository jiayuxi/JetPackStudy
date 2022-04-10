package com.jiayx.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.Test

/**
 *Created by yuxi_
on 2022/4/6
 */
class ChannelTest {
    @Test
    fun `test iterate channel`() = runBlocking {
        val channel = Channel<Int>(Channel.UNLIMITED)
        // 生产者
        val producer = GlobalScope.launch {
            for (i in 1..5) {
                channel.send(i)
                println("${i * i}")
            }
        }
        //消费者
        val consume = GlobalScope.launch {
//            val iterator = channel.iterator()
//            while (iterator.hasNext()) {
//                val element = iterator.next()
//                println("element:$element")
//                delay(2000)
//            }
            for (element in channel){
                println("element:$element")
                delay(2000)
            }
        }
        joinAll(producer, consume)
    }
}