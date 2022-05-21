package com.jiayx.flow.channel

import com.jiayx.flow.bean.CounterMsg
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.sql.SQLOutput
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 *Created by yuxi_
on 2022/4/2
 */

fun main() {
//    channel基础学习()
//    produce构建通道生产者()
//    管道()
//    channelFlow函数()
//    扇出函数()
//    扇入函数()
//    带缓冲的channel()
//    通道公平函数()
//    callbackFlow函数()
//    broadcastChannel函数()
//    testIteratechannel迭代()
    actor启动一个消费者协程()
    produce启动一个生产者者协程()
//    channel关闭()
}

/**
 * channel 基础
 * public fun <E> Channel(
 * capacity: Int = RENDEZVOUS,//缓冲队列的容量
 * onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,// 被压的策略
 * onUndeliveredElement: ((E) -> Unit)? = null // 在元素被发送但未交付给消费者时调用代码块
 * ): Channel<E>
 *  capacity 参数为缓冲区容量，通常是以 Channel 中定义的常量为值
 *
 *  RENDEZVOUS ：默认无锁、无缓冲区，只有消费端调用时，才会发送数据，否则挂起发送操作。
 *               当缓存策略不为BufferOverflow.SUSPEND时，会创建缓冲区容量为1的ArrayChannel。
 *
 *  CONFLATED： 队列容量仅为一，且onBufferOverflow参数只能为BufferOverflow.SUSPEND。
 *              缓冲区满时，永远用最新元素替代，之前的元素将被废弃。
 *              创建实现类ConflatedChannel
 *              内部会使用ReentrantLock对发送与接收元素操作进行加锁，线程安全。
 *
 *  UNLIMITED：无限制容量，缓冲队列满后，会直接扩容，直到OOM。
 *             内部无锁，永远不会挂起
 *
 *  BUFFERED：默认创建64位容量的缓冲队列，当缓存队列满后，会挂起发送数据，直到队列有空余。
 *            创建实现类ArrayChannel，内部会使用ReentrantLock对发送与接收元素操作进行加锁，线程安全。
 *
 *  自定义容量：当capacity容量为1，且onBufferOverflow为BufferOverflow.DROP_OLDEST时，
 *            由于与CONFLATED工作原理相同，会直接创建为实现类ConflatedChannel。
 *            其他情况都会创建为实现类ArrayChannel。
 *
 */
fun `channel基础学习`() = runBlocking {
    val channel = Channel<Int>()
    //通道发送数据
    // isClosedForSend : 实验性质API，为ture时表示Channel已经关闭，停止发送。
    val job = launch {
        for (i in 1..5) {
            if (!channel.isClosedForSend) {
                channel.send(i * i)
            }
            if (i == 3) channel.close()
        }
    }
    // 通道接收数据
    for (i in channel) {
        println(i)
    }
    println("Done")
    job.cancelAndJoin()
    println()
}

/**
 * 构建通道生产者
 * 这里有一个名为 produce 的便捷的协程构建器
 * 并且我们使用扩展函数 consumeEach 在消费者端替代 for 循环
 * 同时官方还提供了一个CoroutineScope的拓展函数produce，用于快速启动生产者协程，并返回ReceiveChannel。
 * 其内部实际是在协程构建中创建Channel，用以发送数据。
 */
fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (i in 1..5) send(i * i)
    close()
    awaitClose {
        println("awaitClose")
    }
}

fun `produce构建通道生产者`() = runBlocking {
    val produceSquares = produceSquares()
    produceSquares.consumeEach { println("接收到的值value: $it") }
    println("Done")
    coroutineContext.cancelChildren() // 取消子协程
    println()
}

/**
 * 管道
 *  receive : 如果在Channel被关闭后，调用receive去取值，会抛出ClosedReceiveChannelException异常。
 *  receiveCatching ： 挂起函数，功能与receive相同，只是防止在缓冲队列关闭时突然抛出异常导致程序崩溃，
 *                     会返回ChannelResult包裹取出的元素值，同时表示当前操作的状态
 *  iterator : 挂起函数，接收Channel时，允许使用for循环进行迭代
 *
 */
fun `管道`() = runBlocking {
    val numbers = produceNumbers()
    val square = square(numbers)
//    val iterator = square.iterator()
//    while (iterator.hasNext()){
//        println("value: ${iterator.next()}")
//    }

    repeat(5) {
//        println("value: ${square.receive()}")
//        println(
//            "value : isClosed : ${square.receiveCatching().isClosed} ," +
//                    "isFailure : ${square.receiveCatching().isFailure} , " +
//                    "isSuccess: ${square.receiveCatching().isSuccess} ," +
//                    "value: ${square.receiveCatching().getOrNull()}"
//        )
           println(
            "value : isClosed : ${square.tryReceive().isClosed} ," +
                    "isFailure : ${square.tryReceive().isFailure} , " +
                    "isSuccess: ${square.tryReceive().isSuccess} ," +
                    "value: ${square.tryReceive().getOrNull()}"
        )
        println()
//        square.consumeEach {
//            println("consumeEach value : $it")
//        }
    }

    println("Done")
    coroutineContext.cancelChildren() // 取消子协程
    println()
}

fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1
    while (true) send(x++) // 从 1 开始的无限的整数流
}

fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) {
        if (!isClosedForSend) {
            send(x * x)
        }
    }
    close()
}
/**
 * Actors
 * 启动一个消费者协程
 */

fun `actor启动一个消费者协程`() = runBlocking {
    val sendChannel: SendChannel<Int> = GlobalScope.actor {
        while (true) {
            val element = receive()
            println("value: $element")
        }
    }
    val produce = GlobalScope.launch {
        (1..5).forEach {
            sendChannel.send(it)
        }
    }
    produce.join()
    println()
}

/**
 * Actors
 * 启动一个生产者协程
 */

fun `produce启动一个生产者者协程`() = runBlocking {
    val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce<Int> {
        (1..5).forEach {
            delay(1000)
            send(it)
        }
    }
    val consumer = GlobalScope.launch {
        for (i in receiveChannel){
            println("received: $i")
        }
    }
    consumer.join()
    println()
}
/**
 *  channel 迭代 iterator
 */
private fun `testIteratechannel迭代`() = runBlocking {
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
        for (element in channel) {
            println("element:$element")
            delay(2000)
        }
    }
    joinAll(producer, consume)
    channel.cancel()
    println()
}


/**
 * channel 的关闭
 */

fun `channel关闭`() = runBlocking {
    val channel = Channel<Int>(3)
    //生产者
    val produce = GlobalScope.launch {
        List(3) {
            channel.send(it)
            println("send: $it")
        }
        channel.close()
        println(
            """close channel . 
            | -ClosedForSend: ${channel.isClosedForSend} 
            | -ClosedReceive: ${channel.isClosedForReceive}""".trimMargin()
        )
    }
    //消费者
    val consumer = GlobalScope.launch {
        for (element in channel) {
            println("receive : $element")
            delay(1000)
        }
        println(
            """close channel . 
            | -ClosedForSend: ${channel.isClosedForSend} 
            | -ClosedReceive: ${channel.isClosedForReceive}""".trimMargin()
        )
    }
    joinAll(produce, consumer)
    println()
}

/**
 * broadcastChannel
 *
 * 多个接收端不存在互斥行为
 */

fun `broadcastChannel函数`() = runBlocking {
//    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
    // channel 转化为 broadcastChannel
    val channel = Channel<Int>()
    val broadcastChannel = channel.broadcast(3)
    val produce = GlobalScope.launch {
        List(3) {
            delay(100)
            broadcastChannel.send(it)
        }
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
    println()
}



/**
 * 扇出
 * produce : 启动一个消费者协程的方法,其它协程就可以用这个channel来接收数据
 * 多个协程也许会接收相同的管道，在它们之间进行分布式工作
 * CoroutineScope 的扩展函数
 * 里面持有 this 对象，this对象指向的是协程作用域 CoroutineScope
 */
fun CoroutineScope.produceNum(): ReceiveChannel<Int> = produce {
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
    println()
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
    println()
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
    println()
}

fun `通道公平函数`() = runBlocking {
    val table = Channel<Ball>()// 一个共享的 table（桌子）
    launch { player("ping", table) }
    launch { player("pong", table) }
    table.send(Ball(0)) // 乒乓球
    delay(1000) // 延迟 1 秒钟
    coroutineContext.cancelChildren() // 游戏结束，取消它们
    println()
}

/**
 * callbackFlow
 */
val callbackFlow = callbackFlow<Int> {
    (1..1000).asFlow().onEach { delay(1000) }.collect {
        val trySend = trySend(it)
        trySend.onClosed {
            println("callbackFlow onClosed")
        }.onFailure {
            println("callbackFlow onFailure")
        }.onSuccess {
            println("callbackFlow onSuccess")
        }
    }
    awaitClose {
        println("collector is closed")
    }
    println()
}

fun `callbackFlow函数`() = runBlocking {
    var count = AtomicInteger()
    try {
        callbackFlow.catch { e -> println("Caught : $e") }.collect {
            count.incrementAndGet()
            println("reault value : ${count.get()}")
        }
        delay(3000)
        coroutineContext.cancelChildren()
    } catch (e: Exception) {
        println("Caught : $e")
    }
    println()
}
/**
 * ChannelFlow
 *
 */

fun `channelFlow函数`() = runBlocking {
    val flow: Flow<String> = channelFlow {
        send("11")
        println("send first on ${Thread.currentThread()}")
        withContext(Dispatchers.IO) {
            send("22")
            println("send second on ${Thread.currentThread()}")
        }
        send("33")
        println("send third on ${Thread.currentThread()}")
        awaitClose {
            println("关闭 awaitClose")
        }
        println()
    }

    val job = launch {
        flow.collect {
            println("result : $it")
        }
    }
    delay(200)
    job.cancel()//交由外部协程控制channel通道关闭
//    coroutineContext.cancelChildren() // 取消子协程
    flow.cancellable()
    println()
}


