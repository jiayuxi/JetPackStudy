package com.jiayx.flow.channel

import android.widget.TableLayout
import dalvik.system.DelegateLastClassLoader
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger

/**
 *Created by yuxi_
on 2022/4/2
 */

fun main() {
//    channel基础学习()
//    produce构建通道生产者()
//    管道()
//    notsafeconcurrent()
//    AtomicInt并发安全方式1()
//    mutex并发安全方式2()
//    semaphore并发安全方式3()
//    testavoidaccessoutervariable()
//    select选择表达式()
//    channelFlow函数()
//    扇出函数()
//    扇入函数()
//    带缓冲的channel()
    通道公平函数()
}

/**
 * channel 基础
 */
fun `channel基础学习`() = runBlocking {
    val channel = Channel<Int>()
    //通道发送数据
    launch {
        for (i in 1..5) channel.send(i * i)
        channel.close()
    }
    // 通道接收数据
    for (i in channel) {
        println(i)
    }
    println("Done")
}

/**
 * 构建通道生产者
 * 这里有一个名为 produce 的便捷的协程构建器
 * 并且我们使用扩展函数 consumeEach 在消费者端替代 for 循环
 */
fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (i in 1..5) send(i * i)
}

fun `produce构建通道生产者`() = runBlocking {
    val produceSquares = produceSquares()
    produceSquares.consumeEach { println("接收到的值value: $it") }
    println("Done")
}

/**
 * 管道
 */
fun `管道`() = runBlocking {
    val numbers = produceNumbers()
    val square = square(numbers)
    repeat(5) {
        println("value: ${square.receive()}")
    }
    println("Done")
    coroutineContext.cancelChildren() // 取消子协程
}

fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1
    while (true) send(x++) // 从 1 开始的无限的整数流
}

fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) send(x * x)
}

/**
 * 协程上的并发
 * 并发安全
 */
/**
 * 不安全的并发访问
 */

fun `notsafeconcurrent`() = runBlocking {
    var count = 0
    List(1000) {
        GlobalScope.launch {
            count++
        }
    }.joinAll()
    println("并发不全 result value: $count")
    println()
}

/**
 * 并发安全的 方式 1
 * 使用AtomicXXX
 */
fun `AtomicInt并发安全方式1`() = runBlocking {
    var atomic = AtomicInteger(0)
    List(1000) {
        GlobalScope.launch {
            atomic.incrementAndGet()
        }
    }.joinAll()
    println("并发安全方式 1 value : ${atomic.get()}")
    println()
}

/**
 * 并发安全 方式 2
 * Mutex
 * Mutex：轻量级锁，它的lock和unlock从语义上与线程锁比较类似，
 * 之所以轻量是因为它在获取不到锁时不会阻塞线程，
 * 而是挂起等待锁的释放
 */

fun `mutex并发安全方式2`() = runBlocking {
    var count = 0
    val mutex = Mutex()
    List(1000) {
        GlobalScope.launch {
            mutex.withLock {
                count++
            }
        }
    }.joinAll()
    println("并发安全的方式2：value: $count")
    println()
}

/**
 * 并发安全方式 3
 * Semaphore 信号量
 */
fun `semaphore并发安全方式3`() = runBlocking {
    var count = 0
    val semaphore = Semaphore(1)
    List(1000) {
        GlobalScope.launch {
            semaphore.withPermit {
                count++
            }
        }
    }.joinAll()
    println("并发安全方式3 vale: $count")
    println()
}

/**
 * 避免访问外部可变状态
 */
fun `testavoidaccessoutervariable`() = runBlocking {
    var count = 0
    val result = count + List(1000) {
        GlobalScope.async { 1 }
    }.sumOf {
        it.await()
    }
    println("避免访问外部可变状态：$result")
    println()
}

/**
 * 选择表达式
 * select
 * 其允许同时等待多个挂起的结果，并且只取用其中最快完成的作为函数恢复的值。
 */

fun `select选择表达式`() = runBlocking {
    val d1 = async {
        delay(60)
        1
    }
    val d2 = async {
        delay(50)
        2
    }
    val d3 = async {
        delay(70)
        3
    }
    val data = select<Int> {
        d3.onAwait { data ->
            println("d3 first result $data")
            data
        }
        d1.onAwait { data ->
            println("d1 first result $data")
            data
        }
        d2.onAwait { data ->
            println("d2 first result $data")
            data
        }
    }
    //由于第2项Deferred是最先通过await获取到值的，所以select也是以其作为返回值。
    //d2 first result 2
    //result: 2
    println("result: $data")

    println()
}

/**
 * ChannelFlow
 *
 */

fun `channelFlow函数`() = runBlocking {
    val flow = channelFlow<String> {
        send("11")
        println("send first on ${Thread.currentThread()}")
        withContext(Dispatchers.IO) {
            send("22")
            println("send second on ${Thread.currentThread()}")
        }
        send("33")
        println("send third on ${Thread.currentThread()}")
        awaitClose {
            println("awaitClose")
        }
    }

    val job = launch {
        flow.collect {
            println("result : $it")
        }
    }
    delay(200)
    job.cancel()//交由外部协程控制channel通道关闭
}

/**
 * 扇出
 * 多个协程也许会接收相同的管道，在它们之间进行分布式工作
 */
fun CoroutineScope.produceNum() = produce<Int> {
    var x = 1
    while (true) {
        send(x++)
        delay(100)
    }
}

//定义处理器协程
fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        println("Processor #$id received $msg")
    }
}

fun `扇出函数`() = runBlocking {
    val producer = produceNum()
    repeat(5) { launchProcessor(it, producer) }
    delay(950)
    producer.cancel() // 取消协程生产者从而将它们全部杀死
}

/**
 * 扇入
 * 多个协程可以发送到同一个通道
 */
suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}

fun `扇入函数`() = runBlocking {
    val channel = Channel<String>()
    launch { sendString(channel, "foo", 200L) }
    launch { sendString(channel, "BAR!", 500L) }
    repeat(6) { // 接收前六个
        println(channel.receive())
    }
    coroutineContext.cancelChildren() // 取消所有子协程来让主协程结束
}

/**
 * 带缓冲的 channel
 * 缓冲允许发送者在被挂起前发送多个元素，
 * 就像 BlockingQueue 有指定的容量一样，
 * 当缓冲区被占满的时候将会引起阻塞
 */
fun `带缓冲的channel`() = runBlocking {
    val channel = Channel<Int>(4)
    //启动一个发送者协程
    val send = launch {
        repeat(10) {
            println("Sending $it") // 在每一个元素发送前打印它们
            channel.send(it) // 将在缓冲区被占满时挂起
        }
    }
    for (msg in channel) {
        println("result: $msg")
    }
    delay(1000)
    send.cancel()
    coroutineContext.cancelChildren() // 游戏结束，取消它们
    println()
}

/**
 * 通道是公平的
 */
data class Ball(var hits: Int)

suspend fun player(name: String, channel: Channel<Ball>) {
    for (ball in channel) {
        ball.hits++
        println("$name $ball")
        delay(300) // 等待一段时间
        channel.send(ball) // 将球发送回去
    }
}

fun `通道公平函数`() = runBlocking {
    val table = Channel<Ball>()// 一个共享的 table（桌子）
    launch { player("ping", table) }
    launch { player("pong", table) }
    table.send(Ball(0)) // 乒乓球
    delay(1000) // 延迟 1 秒钟
    coroutineContext.cancelChildren() // 游戏结束，取消它们
}
/**
 *
 */

