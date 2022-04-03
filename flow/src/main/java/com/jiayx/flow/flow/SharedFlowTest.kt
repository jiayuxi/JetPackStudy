package com.jiayx.flow.flow

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharedFlow as SharedFlow1

/**
 *Created by yuxi_
on 2022/4/2
sharedFlow 共享数据流
SharedFlow本身的定义很简单，只比Flow多个历史数据缓存的集合，只允许订阅数据
MutableShardFlow 定义接收与发送数据
 */

fun main() {
//    mutableSharedFlow函数()
//    sharedFlow函数只能订阅()
    flow转化为SharedFlow()
}
/**
 * sharedFlow
 * SharedFlow即共享的Flow，可以实现一对多关系,SharedFlow是一种热流
 */
//public fun <T> MutableSharedFlow(
//    replay: Int = 0,
//    extraBufferCapacity: Int = 0,
//    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
//): MutableSharedFlow<T>
//其主要有3个参数
//1.replay表示当新的订阅者Collect时，发送几个已经发送过的数据给它，默认为0，即默认新订阅者不会获取以前的数据
//2.extraBufferCapacity表示减去replay，MutableSharedFlow还缓存多少数据，默认为0
//3.onBufferOverflow表示缓存策略，即缓冲区满了之后Flow如何处理，默认为挂起

/**
 * 冷流转化为热流
 * flow 通过 shareIn 转为为 sharedFlow
 * @param scope 共享开始时所在的协程作用域范围
 * @param started 控制共享的开始和结束的策略
 * @param replay 状态流的重播个数
 */
//started 接受以下的三个值:
//1.Lazily: 当首个订阅者出现时开始，在scope指定的作用域被结束时终止。
//2.Eagerly: 立即开始，而在scope指定的作用域被结束时终止。
//3.WhileSubscribed: 这种情况有些复杂，后面会详细讲解
//对于那些只执行一次的操作，您可以使用Lazily或者Eagerly。然而，如果您需要观察其他的流，就应该使用WhileSubscribed来实现细微但又重要的优化工作

//public fun <T> Flow<T>.shareIn(
//    scope: CoroutineScope,
//    started: SharingStarted,
//    replay: Int = 0
//)
//WhileSubscribed
//可以说是Lazily策略的进阶版，同样是等待第一个消费者订阅后，才开始发送数据源。
//但其可以配置在最后一个订阅者关闭后，共享数据流上游停止的时间（默认为立即停止），与历史数据缓存清空时间（默认为永远保留）
//public fun WhileSubscribed(
//   stopTimeoutMillis: Long = 0, //上游数据流延迟结束，ms
//replayExpirationMillis: Long = Long.MAX_VALUE //缓冲数据清空延迟,ms
//): SharingStarted

fun `flow转化为SharedFlow`() = runBlocking {
    val flow = flow<Int> {
        (1..2).forEach { emit(it) }
    }
    val sharedFlow = flow.shareIn(this, SharingStarted.Lazily, 0)
    launch {
        sharedFlow.collect {
            println("冷流转化为热流：$it")
        }
    }
}

/**
 * MutableSharedFlow 的创建
 * 订阅 发送数据
 */
fun `mutableSharedFlow函数`() = runBlocking {
    val sharedFlow = MutableSharedFlow<String>(5)
    val produce = launch(Dispatchers.IO) {
        (1..10).asFlow().onEach { delay(100) }.collect {
            sharedFlow.emit("value:$it")
        }
    }
    delay(500)
    sharedFlow.collect {
        println("result value : $it")
    }
    produce.cancelAndJoin()
    println()
}

/**
 * sharedFlow 只能订阅
 */

fun `sharedFlow函数只能订阅`() = runBlocking {
    val sharedFlow = MutableSharedFlow<String>()
    val job = launch(Dispatchers.IO) {
        (1..10).asFlow().onEach { delay(100) }.collect {
            println(" value : $it")
            sharedFlow.emit("value:$it")
        }
    }
    val readOnlySharedFlow = sharedFlow.asSharedFlow()
    val scope = CoroutineScope(SupervisorJob())
    delay(100)
    val job1 = scope.launch { //消费者单独一个协程
        readOnlySharedFlow.map {
            println("map : $it")
            delay(100)
            "$it receive 1"
        }
            .collect {
                println("collect1 result : $it")
            }
    }
}
