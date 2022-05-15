package com.jiayx.flow.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

/**
 *Created by yuxi_
on 2022/5/15
 todo flow 创建操作符
 */
fun main() {
    创建flow()
    callbackFlow()
}

/**
 * 创建 flow
 */
fun `创建flow`() = runBlocking {
    val flow = flow<Int> {
        emit(1)
    }
    coroutineScope {
        flow.collect {
            println("flow value : $it")
        }
    }
    println()
    // 快速创建flow 类比listOf
    val flow2 = flowOf(1, 2, 3)
    coroutineScope {
        flow2.collect {
            println("flowOf value: $it")
        }
    }
    println()
    // asFlow
    val flow3 = listOf(1, 2, 3).asFlow()
    coroutineScope {
        flow3.collectLatest {
            println("asFlow value: $it")
        }
    }
    println()
}

/**
 * callbackFlow
 * 与 flow 构建器不同，callbackFlow 允许通过 send 函数从不同 CoroutineContext 发出值，
 * 或者通过 offer/trySend 函数在协程外发出值。
 * 在协程内部，callbackFlow 会使用通道，它在概念上与阻塞队列非常相似。通道都有容量配置，
 * 限定了可缓冲元素数的上限。在 callbackFlow 中所创建通道的默认容量为 64 个元素。
 * 当您尝试向完整通道添加新元素时，send 会将数据提供方挂起，直到新元素有空间为止，
 * 而 offer 不会将相关元素添加到通道中，并会立即返回 false
 */
interface ICallBack {
    fun onSuccess(sucStr: String?)
    fun onError(error: Exception)
}

private fun getSearchCallbackFlow(): Flow<Boolean> = callbackFlow {
    val callback = object : ICallBack {
        override fun onSuccess(sucStr: String?) {
            //搜索目的地成功
            trySend(true)
        }

        override fun onError(error: Exception) {
            //搜索目的地失败
            trySend(false)
        }
    }
    //模拟网络请求
    Thread {
        Thread.sleep(500)
        //模拟Server返回数据
        callback.onSuccess("getServerInfo")
    }.start()

    //这是一个挂起函数, 当 flow 被关闭的时候 block 中的代码会被执行 可以在这里取消接口的注册等
    awaitClose { println("awaitClose") }
}

private fun goDesCallbackFlow(isSuc: Boolean): Flow<String?> = callbackFlow {
    val callback = object : ICallBack {
        override fun onSuccess(sucStr: String?) {
            trySend(sucStr)
        }

        override fun onError(error: Exception) {
            trySend(error.message)
        }
    }
    //模拟网络请求
    Thread {
        Thread.sleep(500)
        if (isSuc) {
            //到达目的地
            callback.onSuccess("arrive at the destination")
        } else {
            //发生了错误
            callback.onError(IllegalArgumentException("Not at destination"))
        }
    }.start()

    awaitClose { println("awaitClose") }
}

private fun `callbackFlow`() = runBlocking {
    var count = AtomicInteger()
    coroutineScope {
        //同一作用域下，协程挂起，后面的 函数不会执行
    }
   val job = launch {
        getSearchCallbackFlow()
            .flatMapConcat {
                goDesCallbackFlow(it)
            }.collect {
                println("search callback value :$it")
            }
    }
    val job2 = launch {
        goDesCallbackFlow(false).collect{
            println("des callback value :$it")
        }
    }
    delay(2000)
    job.cancelAndJoin()
    job2.cancelAndJoin()
    println()
}


