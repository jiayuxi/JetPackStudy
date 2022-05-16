package com.jiayx.flow.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
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
//    flow转化为StateFlow()
//    stateFlow函数()
    stateFlow简单实用()
}
/*
* public fun <T> MutableStateFlow(value: T): MutableStateFlow<T> = StateFlowImpl(value ?: NULL)
*  注意：其中有一个主要参数
*  value 默认值
* 1.StateFlow构造函数较为简单，只需要传入一个默认值
* 2.StateFlow本质上是一个replay为1，并且没有缓冲区的SharedFlow,因此第一次订阅时会先获得默认值
* 3.StateFlow仅在值已更新，并且值发生了变化时才会返回，即如果更新后的值没有变化，
* 也没会回调Collect方法，这点与LiveData不同
* */
/**
 * 冷流转化为热流
 * flow 通过 shareIn 转为为 sharedFlow
 *@param scope 共享开始时所在的协程作用域范围
 *@param started 控制共享的开始和结束的策略
 *@param initialValue 状态流的初始值
 */
/*
public fun <T> Flow<T>.stateIn(
    scope: CoroutineScope,
    started: SharingStarted,
    initialValue: T
): StateFlow<T> */

//started 接受以下的三个值:
//1.Lazily: 当首个订阅者出现时开始，在scope指定的作用域被结束时终止。
//2.Eagerly: 立即开始，而在scope指定的作用域被结束时终止。
//3.WhileSubscribed: 这种情况有些复杂，后面会详细讲解
//对于那些只执行一次的操作，您可以使用Lazily或者Eagerly。然而，如果您需要观察其他的流，就应该使用WhileSubscribed来实现细微但又重要的优化工作

//WhileSubscribed
//可以说是Lazily策略的进阶版，同样是等待第一个消费者订阅后，才开始发送数据源。
//但其可以配置在最后一个订阅者关闭后，共享数据流上游停止的时间（默认为立即停止），与历史数据缓存清空时间（默认为永远保留）
//public fun WhileSubscribed(
//   stopTimeoutMillis: Long = 0, //上游数据流延迟结束，ms
//replayExpirationMillis: Long = Long.MAX_VALUE //缓冲数据清空延迟,ms
//): SharingStarted
//replayExpirationMillis 配置了以毫秒为单位的延迟时间，定义了从停止共享协程到重置缓存
// (恢复到 stateIn 运算符中定义的初始值 initialValue) 所需要等待的时间。
// 它的默认值是长整型的最大值 Long.MAX_VALUE (表示永远不将其重置)。如果设置为 0，
// 可以在符合条件时立即重置缓存的数据。
//
fun `flow转化为StateFlow`() = runBlocking {
    val flow = flow<Int> {
        (1..2).forEach { emit(it) }
    }
    val resultStateFlow = flow.stateIn(this, started = WhileSubscribed(5000), initialValue = 0)
    launch {
        resultStateFlow.collect {
            println("flow 转化为 StateFlow value:$it")
        }
    }
    println()
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
    val stateFlow = MutableStateFlow(1)//默认值
    val readOnlyStateFlow = stateFlow.asStateFlow()

    //模拟外部立即订阅数据
    val job0 = launch {
        readOnlyStateFlow.collect { println("collect0 : $it") }
        //StateFlow订阅者所在的协程，最好使用独立协程，collect会一直挂起，协程内的后续操作不会执行
//        readOnlyStateFlow.collect { println("collect1 : $it") }
//        println("订阅 stateFlow 协程")
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