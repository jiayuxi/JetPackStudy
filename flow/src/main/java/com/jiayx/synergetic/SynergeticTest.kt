package com.jiayx.synergetic

import com.jiayx.flow.bean.CounterMsg
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 *Created by yuxi_
 * on 2022/4/3
 * 协程
 * 共享的可变状态与并发
 */

fun main() {
    非线程安全的共享的可变状态与并发()
    AtomicInteger线程安全的结构性并发()
    以细粒度限制线程()
    以粗粒度限制线程()
    mutex锁解决方案()
    Semaphore解决方案()
    actors函数()
}

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 10  // 启动的协程数量
    val k = 100 // 每个协程重复执行同一动作的次数
    val time = measureTimeMillis {
        coroutineScope { // 协程的作用域
            repeat(n) {
                launch {
                    repeat(k) { action() }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}

/**
 * Volatile
 * 因为 volatile 变量保证可线性化（这是“原子”的技术术语）读取和写入变量，
 * 但在大量动作（在我们的示例中即“递增”操作）发生时并不提供原子性。
 */
@Volatile
var counter = 0
fun `非线程安全的共享的可变状态与并发`() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            counter++
        }
    }
    println("Counter = $counter")
    println()
}

/**
 * 线程安全的数据结构
 * AtomicInteger
 */
var atomic = AtomicInteger()
fun `AtomicInteger线程安全的结构性并发`() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            atomic.incrementAndGet()
        }
    }
    println(" AtomicInteger Counter = ${atomic.get()}")
    println()
}

/**
 * 以细粒度限制线程
 * 限制线程 是解决共享可变状态问题的一种方案：对特定共享状态的所有访问权都限制在单个线程中
 */
val counterContext = newSingleThreadContext("CounterContext")
var counter2 = 0
fun `以细粒度限制线程`() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            // 将每次自增限制在单线程上下文中
            withContext(counterContext) {
                counter2++
            }
        }
    }
    println("以细粒度限制线程 Counter = $counter2")
    println()
}

/**
 * 以粗粒度限制线程
 * 线程限制是在大段代码中执行的
 */
var counter3 = 0
fun `以粗粒度限制线程`() = runBlocking {
    // // 将一切都限制在单线程上下文中
    withContext(counterContext) {
        massiveRun {
            counter3++
        }
    }
    println("以粗粒度限制线程 Counter = $counter3")
    println()
}
/**
 * 互斥的解决方案
 * Mutex
 *  * Mutex：轻量级锁，它的lock和unlock从语义上与线程锁比较类似，
 * 之所以轻量是因为它在获取不到锁时不会阻塞线程，
 * 而是挂起等待锁的释放
 */
val mutex = Mutex()
var counter4 = 0
fun `mutex锁解决方案`() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            // 用锁保护每次自增
            mutex.withLock {
                counter4++
            }
        }
    }
    println("mutex Counter = $counter4")
    println()
}

/**
 * 信号量
 * Semaphore 信号量
 */
val semaphore = Semaphore(1)
var counter5 = 0
fun `Semaphore解决方案`() = runBlocking {
    withContext(Dispatchers.Default){
        massiveRun {
            semaphore.withPermit {
                counter5++
            }
        }
    }
    println("semaphore Counter = $counter4")
    println()
}

private fun Semaphore.withPermit(action: () -> Any) {}

/**
 * Actors
 */
// 这个函数启动一个新的计数器 actor
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // actor 状态
    for (msg in channel) { // 即将到来消息的迭代器
        when (msg) {
            is CounterMsg.IncCounter -> counter++
            is CounterMsg.GetCounter -> msg.response.complete(counter)
        }
    }
}
fun `actors函数`() = runBlocking {
    val counter = counterActor() // 创建该 actor
    withContext(Dispatchers.Default) {
        massiveRun {
            counter.send(CounterMsg.IncCounter)
        }
    }
    // 发送一条消息以用来从一个 actor 中获取计数值
    val response = CompletableDeferred<Int>()
    counter.send(CounterMsg.GetCounter(response))
    println(" actors Counter = ${response.await()}")
    counter.close() // 关闭该actor
}
