package com.coroutine.test.coroutinesCamp

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 *Created by yuxi_
on 2022/4/3
 async 组合挂起函数
 async 组合型并发
 */

fun main() {
//    默认顺序调用()
//    使用async并发()
//    惰性启动的async()
//    使用async的结构化并发()
//    取消始终通过协程的层次结构来进行传递()
//    launch与async返回值比较()
    join等到协程作业()
    async组合并发()
    UNDISPATCHED()
    DEFAULT()
    ATOMIC()
    LAZY()
}

/**
 * 默认顺序调用
 */
suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // 假设我们在这里做了些有用的事
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // 假设我们在这里也做了一些有用的事
    return 29
}

fun `默认顺序调用`() = runBlocking {
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")

    println()
}

/**
 * 使用 async 并发
 */
fun `使用async并发`() = runBlocking {
    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
    println()
}

/**
 * 惰性启动的 async
 * 可选的，async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
 * 在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候
 */
fun `惰性启动的async`() = runBlocking {
    val time = measureTimeMillis {
        try {
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
//            val one = async() { doSomethingUsefulOne() }
//            val two = async() { doSomethingUsefulTwo() }
//            one.cancel()
            //todo 调用start 方式
            one.start()
            two.start()
            //todo await 获取的时候协程才会启动
            println("The answer is ${one.await() + two.await()}")
        } catch (e: Exception) {
            println("异常：$e")
        }
    }
    println("Completed in $time ms")
    println()
}

/**
 * 使用 async 的结构化并发
 */
suspend fun concurrentSum() = coroutineScope {
    val one = async(Dispatchers.Default) {
        log("one 结构性并发")
        doSomethingUsefulOne()
    }
    val two = async() {
        log("two 结构性并发")
        doSomethingUsefulTwo()
    }
    one.await() + two.await()
}

fun `使用async的结构化并发`() = runBlocking {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
    println()
}

/**
 * 取消始终通过协程的层次结构来进行传递
 * 如果在 failedConcurrentSum 函数内部发生了错误，并且它抛出了一个异常， 所有在作用域中启动的协程都会被取消。
 */
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE) // 模拟一个长时间的运算
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}

fun `取消始终通过协程的层次结构来进行传递`() = runBlocking {
    try {
        failedConcurrentSum()
    } catch (e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }
}

/**
 *launch 与 async 返回值比较
 */
fun `launch与async返回值比较`() = runBlocking {
    val job1 = launch {
        delay(300)
        println("job1 finished")
    }

    val job2 = async {
        delay(200)
        println("job2 finished")
        "job2 result"
    }
    println(job2.await())
}

/**
 *launch join 等到协程作业
 */

fun `join等到协程作业`() = runBlocking {
    println()
    val job1 = launch {
        delay(2000)
        println("job1 finished")
    }
    job1.join()
    val job2 = launch {
        delay(200)
        println("job2 finished")
    }
    val job3 = launch {
        delay(200)
        println("job3 finished")
    }
    println()
}

/**
 * async 组合并发 await 等待协程作业
 */

fun `async组合并发`() = runBlocking {
    println()
    val job1 = async {
        delay(2000)
        println("job1 finished")
    }
//    job1.join()
    job1.await()
    val job2 = async {
        delay(200)
        println("job2 finished")
    }
    val job3 = async {
        delay(200)
        println("job3 finished")
    }
    println()
}

/**
 * UNDISPATCHED ： 协程创建后立即在当前函数调用栈中执行，直到遇到第一个真正挂起的点
 *  // 遇到第一个挂起的点，切换到 IO 线程中
 *  直到遇到第一个挂起的点 才能相应取消状态
 */
fun `UNDISPATCHED`() = runBlocking {
    val time = measureTimeMillis {
        try {
            val one = async(context = Dispatchers.IO, start = CoroutineStart.UNDISPATCHED) {
                //立即直接运行，在main 线程中
                println("thread: ${Thread.currentThread().name}")
                // 遇到第一个挂起的点，切换到 IO 线程中 ，
                doSomethingUsefulOne()
                // DefaultDispatcher-worker-1 工作线程
                println("thread: ${Thread.currentThread().name}")
            }
            // 执行一些计算
            one.start() // 启动第一个
//            one.cancel()
            println("The answer is ${one.await()}")
        } catch (e: Exception) {
            println("异常：$e")
        }
    }
    println("Completed in $time ms")

}

/**
 *DEFAULT
 *  // TODO 立即开始调度
 *  在调度前协程被取消，其将直接进入协程取消响应状态
 */

fun `DEFAULT`() = runBlocking {
    println()
    val time = measureTimeMillis {
        try {
            val one = async(/*context = Dispatchers.IO,*/ start = CoroutineStart.DEFAULT) {
                // TODO 立即开始调度
                // 运行在IO 线程中 ，
                println("thread1: ${Thread.currentThread().name}")
                doSomethingUsefulOne()
                // DefaultDispatcher-worker-1 工作线程
                println("thread2: ${Thread.currentThread().name}")
            }
            // 执行一些计算
            one.start() // 启动第一个
            delay(200)
            one.cancel()//
            println("The answer is ${one.await()}")
        } catch (e: Exception) {
            println("异常：$e")
        }
    }
    println("Completed in $time ms")
    println()
}

/**
 * ATOMIC : 协程创建后，立即开始调度，协程执行到第一个挂起函数点前不响应取消
 */

fun `ATOMIC`() = runBlocking {
    println()
    val time = measureTimeMillis {
        try {
            val one = async(/*context = Dispatchers.IO,*/ start = CoroutineStart.ATOMIC) {
                // TODO 立即开始调度
                println("thread1: ${Thread.currentThread().name}")
                doSomethingUsefulOne()
                // DefaultDispatcher-worker-1 工作线程
                println("thread2: ${Thread.currentThread().name}")
            }
            // 执行一些计算
            one.start() // 启动第一个
            one.cancel()// TODO 协程执行到第一个挂起函数点前不响应取消
            println("The answer is ${one.await()}")
        } catch (e: Exception) {
            println("异常：$e")
        }
    }
    println("Completed in $time ms")
    println()
}
/**
 * LAZY
 * 只有协程被需要时，包括主动调用协程的
 * start ,await 或者 join 等函数时才开始调度，
 * 如果调度前就被取消，那么该协程就会直接进入异常结束状态
 */

private fun `LAZY`() = runBlocking {
    try{
    val job = async(start = CoroutineStart.LAZY){
        println("thread1: ${Thread.currentThread().name}")
        doSomethingUsefulOne()
        println("thread2: ${Thread.currentThread().name}")
    }
        job.cancel()
        job.await()
    }catch (e:Exception){
        println("异常：$e")
    }
}