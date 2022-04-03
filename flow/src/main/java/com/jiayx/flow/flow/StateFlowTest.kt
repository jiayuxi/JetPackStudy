package com.jiayx.flow.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *Created by yuxi_
on 2022/4/3
StateFlow
StateFlow 是 SharedFlow 的一个比较特殊的变种
1.它始终是有值的。
2.它的值是唯一的。
3.它允许被多个观察者共用 (因此是共享的数据流)。
4.它永远只会把最新的值重现给订阅者，这与活跃观察者的数量是无关的。

StateFlow订阅者所在的协程，最好使用独立协程，collect会一直挂起，协程内的后续操作不会执行
 */

fun main() {
    stateFlow函数()
    stateFlow简单实用()
}

fun `stateFlow函数`() = runBlocking {
    val stateFlow = MutableStateFlow(0)
    (1..2).asFlow().collect {
        stateFlow.emit(it)
    }
    launch {
        stateFlow.collect {
            println("statFlow value: $it")
        }
    }
    println()
}

fun `stateFlow简单实用`() = runBlocking {
    val stateFlow = MutableStateFlow(1)
    val readOnlyStateFlow = stateFlow.asStateFlow()

    //模拟外部立即订阅数据
    val job0 = launch {
        readOnlyStateFlow.collect { println("collect0 : $it") }
    }
    delay(50)
    //模拟在另一个类发送数据
    launch {
        for (i in 1..3) {
            println("wait emit $i")
            stateFlow.emit(i)
            delay(50)
        }
    }
    //模拟启动页面，在新页面订阅
    delay(200)
    val job1 = launch {
        readOnlyStateFlow.collect { println("collect1 : $it") }
    }
    val job2 = launch {
        readOnlyStateFlow.collect { println("collect2 : $it") }
    }
    println("get value : ${readOnlyStateFlow.value}")
    delay(200)
    job0.cancel()
    job1.cancel()
    job2.cancel()
}