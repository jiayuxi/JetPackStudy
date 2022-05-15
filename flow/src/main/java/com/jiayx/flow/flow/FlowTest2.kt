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
// flow 操作符
fun main() {
    创建flow()
    callbackFlow()
//    tryCatch异常捕获()
//    catch异常捕获()
//    catch异常捕获()


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

/**
 * todo 异常捕获
 *  try catch
 *  捕获了在发射器或任何过渡或末端操作符中发生的任何异常
 */

private fun `tryCatch异常捕获`() = runBlocking {
    try {
        (1..3).asFlow()
            .collect {
                println("last result: $it")
                check(it <= 1) { "Collected $it" }
            }
    } catch (e: Exception) {
        println("Caught $e")
    }
    println()
}

/**
 * 异常透明性
 * catch 异常捕获
 * catch 过渡操作符遵循异常透明性，仅捕获上游异常（catch 操作符上游的异常，但是它下面的不是）。
 * 如果 collect { ... } 块（位于 catch 之下）抛出一个异常，那么异常会逃逸：
 */
private fun `catch异常捕获`() = runBlocking {
    (1..3).asFlow().map { value ->
        check(value <= 1) { "Crashed on $value" }
        "string $value"
    }.catch { e ->
        emit("Caught: $e")
    }.collect {
        println("last catch result : $it")
    }
    println()
}

/**
 * 声明式 捕获异常
 * 我们可以将 catch 操作符的声明性与处理所有异常的期望相结合，
 * 将 collect 操作符的代码块移动到 onEach 中，
 * 并将其放到 catch 操作符之前。
 * 收集该流必须由调用无参的 collect() 来触发
 */
private fun `声明式捕获异常`() = runBlocking {
    (1..3).asFlow()
        .onEach { value ->
            check(value <= 1) { "Collected $value" }
            println("value: $value")
        }
        .catch { e -> println("Caught $e") }
        .collect()
}

