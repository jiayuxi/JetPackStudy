package com.jiayx.flow.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

/**
 *Created by yuxi_
on 2022/5/15
todo flow 功能性操作符
 */
fun main() {
    cancellable()
    tryCatch异常捕获()
    catch异常捕获()
    声明式捕获异常()
    retryWhen()
    retry()
    buffer()
    conflate()
    flowOn()
}

/**
 * cancellable : 接收的的时候判断 协程是否被取消 ，如果已取消，则抛出异常
 */

private fun `cancellable`() = runBlocking {
    val flow = flowOf(1, 2, 3).onEach { delay(50) }

    val job = flow.cancellable().onEach { println("value : $it") }
        .catch { cause -> println("Caught $cause") }.launchIn(this)
    delay(100)
    job.cancel()
}
/**
 * catch : 对上游异常进行捕获 ，对下游无影响
 * 上游 指的是 此操作符之前的流
 * 下游 指的是此操作符之后的流
 */
/**
 *  异常捕获
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

/**
 * retryWhen : 有条件的进行重试 ，lambda 中有两个参数: 一个是 异常原因，一个是当前重试的 index (从0开始).
 * lambda 的返回值 为 Boolean ，true则继续重试 ,false 则结束重试
 */
/**
 * retry、在发生异常时进行重试，
 */

private fun `retryWhen`() = runBlocking {
    coroutineScope {
        simpleRety().retryWhen { cause, attempt ->
            println("attempt : $attempt")
            // 第一种重试方法
//            attempt < 4
            // 第二种重试方法
            if (attempt > 4) {
                return@retryWhen false
            }
            cause is Exception
        }.catch { cause -> println("Caught :$cause") }.collect {
            println("value : $it")
        }
    }
    println()
}

/**
 * retry ： 重试机制 ，当流发生异常时可以重新执行。retryWhen 的简化版。
 * retries: ``Long`` = Long.MAX_VALUE 指定重试次数，以及控制是否继续重试.(默认为true)
 */
private fun `retry`() = runBlocking {
    coroutineScope {
        simpleRety().retry(3) { e ->
            e is IOException
        }.catch { cause -> println("Caught :$cause") }.collect {
            println("value : $it")
        }
    }
    println()
}

private fun simpleRety(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
        throw IOException("抛出异常")
    }
}

/**
 * buffer : 如果操作符的代码需要相当****长时间来执行 ，可使用buffer操作符在执行期间为其创建一个单独的协程
 * capacity: Int = BUFFERED 缓冲区的容量
 * RENDEZVOUS ，无缓存区，生产一个消费一个。
 * BUFFERED，创建个默认缓存容量为64的缓存区
 * CONFLATED ，相当于把缓存容量设置为1，且缓存策略强制为DROP_OLDEST
 * 自定义缓存区容量大小
 * onBufferOverflow: BufferOverflow = BufferOverflow.``SUSPEND **溢出的话执行的操作
 * 有三个选择 ：
 * SUSPEND 挂起，当缓存区满时，将生产者挂起，不继续发送后续值，直到缓冲区有空缺位置。
 * DROP_OLDEST 丢掉旧的，缓存区满时，移除缓存区中最旧的值，并插入最新的值
 * DROP_LATEST 丢掉新的 ，缓存区满时，抛弃最新的值。
 */
private fun `buffer`() = runBlocking {
    val time = measureTimeMillis {
        simpleBuffer().buffer().collect {
            delay(300)
            println("value: $it")
        }
        println()
        simpleBuffer().buffer(2, onBufferOverflow = BufferOverflow.DROP_OLDEST).collect {
            delay(300)
            println("value: $it")
        }
        println()
        simpleBuffer().buffer(1, onBufferOverflow = BufferOverflow.DROP_LATEST).collect {
            delay(300)
            println("value: $it")
        }
    }
    println("消耗的时间：$time")
    println()
}

private fun simpleBuffer(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
}.buffer().flowOn(Dispatchers.IO)

/**
 * conflate : 仅保留最新值, 内部就是 buffer``(``CONFLATED``)
 * 当收集器处理它们太慢的时候， conflate 操作符可以用于跳过中间值
 */
private fun `conflate`() = runBlocking {
    val time = measureTimeMillis {
        simpleBuffer().conflate().collect {
            delay(400)
            println("value: $it")
        }
    }
    println("消耗的时间：$time")
    println()
}

/**
 * flowOn ： 指定上游操作的执行线程 。 想要切换执行线程 就用它！
 * 调度器
 */
private fun `flowOn`() = runBlocking {
    val myDispatcher = Executors.newSingleThreadExecutor()
        .asCoroutineDispatcher()
    flow {
        println("emit on ${Thread.currentThread().name}")
        emit("data")
    }.map {
        println("run first map on${Thread.currentThread().name}")
        "$it map"
    }.flowOn(Dispatchers.IO)
        .map {
            println("run second map on ${Thread.currentThread().name}")
            "${it},${it.length}"
        }.flowOn(myDispatcher)
        .collect {
            println("result $it on ${Thread.currentThread().name}")
        }
    println()
}
