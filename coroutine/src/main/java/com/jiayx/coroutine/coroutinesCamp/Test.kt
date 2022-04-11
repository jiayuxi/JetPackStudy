package com.jiayx.coroutine.coroutinesCamp

import kotlinx.coroutines.*

/**
 *Created by yuxi_
on 2022/4/3
协程学习
 */
fun main() {
//    协程job函数()
//    作用域构建器coroutinesScope()
//    提取函数重构()
//    轻量协程()
//    全局协程像守护线程()
//    取消协程的执行cancel()
//    取消是协作式的()
//    取消的两种方式()
//    在finally中释放资源()
//    运行不能取消的代码块()
//    超时withTimeout()
    超时withTimeoutOrNull()
}

/**
 * 协程 join
 * 结构化的并发
 */
fun `协程job函数`() = runBlocking {
    val job = GlobalScope.launch {
        delay(1000)
        println("word")
    }
    println("Hello")
    job.join()
    launch {//作用域中启动一个新协程
        delay(1000)
        println("word2")
    }
    println()
}

/**
 * 作用域构建器
 * runBlocking 与 coroutineScope 可能看起来很类似，因为它们都会等待其协程体以及所有子协程结束。
 * 主要区别在于，runBlocking 方法会阻塞当前线程来等待，
 * 而 coroutineScope 只是挂起，会释放底层线程用于其他用途。
 * 由于存在这点差异，runBlocking 是常规函数，而 coroutineScope 是挂起函数
 */
fun `作用域构建器coroutinesScope`() = runBlocking {
    launch {
        delay(200)
        println("Task from runBlocking")
    }
    coroutineScope { //创建一个协程作用域
        launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
    }
    println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    println()
}

/**
 * 提取函数重构
 */
suspend fun doWord() {
    delay(1000L)
    println("word")
}

fun `提取函数重构`() = runBlocking {
    launch {
        doWord()
    }
    println("Hello!")
    println()
}

fun `轻量协程`() = runBlocking {
//    repeat(10) { // 启动大量的协程
//        launch {
//            delay(50L)
//            print(".")
//        }
//    }
}

/**
 * 全局协程像守护线程
 * 在 GlobalScope 中启动的活动协程并不会使进程保活。它们就像守护线程。
 */
fun `全局协程像守护线程`() = runBlocking {
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // 在延迟后退出
}

/**
 * 取消协程的执行
 */

fun `取消协程的执行cancel`() = runBlocking {
    val job = launch {
        repeat(1000) {
            println("job: I'm sleeping $it ...")
            delay(500)
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    //第一种方法
//    job.cancel() // 取消该作业
//    job.join() // 等待作业执行结束
    // 第二种方法
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

/**
 * 取消是协作的
 * 协程的取消是 协作 的。一段协程代码必须协作才能被取消。
 * 所有 kotlinx.coroutines 中的挂起函数都是 可被取消的 。
 * 它们检查协程的取消， 并在取消时抛出 CancellationException
 */
fun `取消是协作式的`() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")
    // 调用取消之后 ，检测是否有取消标识，如果没有，就会执行完毕之后自动取消
    // 所有取消是协作式的
    //job: I'm sleeping 0 ...
    //job: I'm sleeping 1 ...
    //job: I'm sleeping 2 ...
    //main: I'm tired of waiting!
    //job: I'm sleeping 3 ...
    //job: I'm sleeping 4 ...
    //main: Now I can quit.
}

/**
 * 取消的两种方式
 */
fun `取消的两种方式`() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
//        while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
//            //todo 第一种取消方式 delay 挂起
//            delay(100)
//            // 每秒打印消息两次
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("job: I'm sleeping ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
        // todo 取消的第二种方式  i < 5 替换成 isActive 显式的检查取消状态
        while (isActive) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
        // todo 取消的第三种方式  ensureActive()
        while (true) { // 一个执行计算的循环，只是为了占用 CPU
             ensureActive()
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }

    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 在 finally 中释放资源
 * 我们通常使用如下的方法处理在被取消时抛出 CancellationException 的可被取消的挂起函数
 */
fun `在finally中释放资源`() = runBlocking {
    val job = launch {
        try {
            repeat(1000) {
                println("job: I'm sleeping $it ...")
                delay(500L)
            }
        } catch (e: CancellationException) {
            println("取消抛出的异常")
        } finally {
            println("job: I'm running finally")
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 运行不能取消的代码块
 * 当你需要挂起一个被取消的协程，
 * 你可以将相应的代码包装在 withContext(NonCancellable) {……} 中，
 * 并使用 withContext 函数以及 NonCancellable 上下文
 */

fun `运行不能取消的代码块`() = runBlocking {
    val job = launch {
        try {
            repeat(1000) {
                println("job: I'm sleeping $it ...")
                delay(500L)
            }
        } finally {
            // todo 使用此方法 挂起一个被取消的协程
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 超时
 *  withTimeout
 */

fun `超时withTimeout`() = runBlocking {
    try {
        withTimeout(1300L) {
            repeat(1000) {
                println("I'm sleeping $it ...")
                delay(500L)
            }
        }
    } catch (e: TimeoutCancellationException) {
        println("超时异常")
    }
}

/**
 * 超时
 * withTimeoutOrNull
 * 并把这些会超时的代码包装在 try {...} catch (e: TimeoutCancellationException) {...}
 * 代码块中，而 withTimeoutOrNull 通过返回 null 来进行超时操作，从而替代抛出一个异常：
 */
fun `超时withTimeoutOrNull`() = runBlocking {
   val result =  withTimeoutOrNull(1300L) {
        repeat(1000) {
            println("I'm sleeping $it ...")
            delay(500L)
        }
    }
    println("Result is $result")
}
