package com.jiayx.coroutine.coroutinesCamp

import kotlinx.coroutines.*
import java.lang.ArithmeticException
import java.lang.AssertionError

/**
 *Created by yuxi_
on 2022/4/3
协程上下文与调度器
协程上下文是各种不同元素的集合。其中主元素是协程中的 Job
 */

fun main() {
//    调度器与线程()
//    非受限调度器vs受限调度器()
//    调试协程与线程()
//    在不同线程间跳转()
//    上下文中的作业()
//    子协程()
//    父协程的职责()
//    组合上下文中的元素()
//    协程作用域()
//    coroutineScope挂起()
//    supervisorScope构造器()
//    取消作用域取消子协程()
//    被取消的子协程不会影响其余的兄弟协程()
//    CancellationException()
//    CoroutineExceptionHandler异常捕获()
//    CoroutineExceptionHandler异常捕获2()
    协程上下文继承()
}

/**
 * 调度器与线程
 * 当调用 launch { …… } 时不传参数，它从启动了它的 CoroutineScope 中承袭了上下文（以及调度器）。
 * 在这个案例中，它从 main 线程中的 runBlocking 主协程承袭了上下文。
 *
 *
 */
fun `调度器与线程`() = runBlocking {
    // todo   Dispatchers.Unconfined 是一个特殊的调度器且似乎也运行在 main 线程中
    launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    // todo 当协程在 GlobalScope 中启动时，使用的是由 Dispatchers.Default 代表的默认调度器。 默认调度器使用共享的后台线程池。
    //  所以 launch(Dispatchers.Default) { …… } 与 GlobalScope.launch { …… } 使用相同的调度器。
    launch { // 运行在父协程的上下文中，即 runBlocking 主协程
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) { // 将会获取默认调度器
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    //todo newSingleThreadContext 为协程的运行启动了一个线程。 一个专用的线程是一种非常昂贵的资源。
    // 在真实的应用程序中两者都必须被释放，当不再需要的时候，使用 close 函数，或存储在一个顶层变量中使它在整个应用程序中被重用。
    launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
    println()
}

/**
 * 非受限调度器 vs 受限调度器
 * Dispatchers.Unconfined 协程调度器在调用它的线程启动了一个协程，但它仅仅只是运行到第一个挂起点。
 * 挂起后，它恢复线程中的协程，而这完全由被调用的挂起函数来决定。
 * 非受限的调度器非常适用于执行不消耗 CPU 时间的任务，以及不更新局限于特定线程的任何共享数据（如UI）的协程
 *
 * 另一方面，该调度器默认继承了外部的 CoroutineScope。 runBlocking 协程的默认调度器，特别是，
 * 当它被限制在了调用者线程时，继承自它将会有效地限制协程在该线程运行并且具有可预测的 FIFO 调度
 */

fun `非受限调度器vs受限调度器`() = runBlocking {
    println()
    launch(Dispatchers.Unconfined) { // 非受限的——将和主线程一起工作
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(500)// TODO 第一个挂起点
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    launch { // 父协程的上下文，主 runBlocking 协程
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }

}

/**
 * 调试协程与线程
 */
fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
fun `调试协程与线程`() = runBlocking {
    val a = async {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")
    println()
}

/**
 * 在不同线程间跳转
 * 在这个例子中，
 * 当我们不再需要某个在 newSingleThreadContext 中创建的线程的时候，
 * 它使用了 Kotlin 标准库中的 use 函数来释放该线程
 */
fun `在不同线程间跳转`() = runBlocking {
    newSingleThreadContext("Ctx1").use { Ctx1 ->
        newSingleThreadContext("Ctx2").use { Ctx2 ->
            runBlocking(Ctx1) {
                log("Started in ctx1")
                withContext(Ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }
    //其中一个使用 runBlocking 来显式指定了一个上下文，
    // 并且另一个使用 withContext 函数来改变协程的上下文，
    // 而仍然驻留在相同的协程中
    //[Ctx1] Started in ctx1
    //[Ctx2] Working in ctx2
    //[Ctx1] Back to ctx1
}

/**
 * 上下文中的作业
 * 协程的 Job 是上下文的一部分，并且可以使用 coroutineContext [Job] 表达式在上下文中检索它
 *
 * todo 请注意，CoroutineScope 中的
 * todo isActive 只是 coroutineContext[Job]?.isActive == true 的一种方便的快捷方式。
 */

fun `上下文中的作业`() = runBlocking {
    println("My job is ${coroutineContext[Job]}")
}

/**
 * 子协程
 *
 * 当一个协程被其它协程在 CoroutineScope 中启动的时候，
 * 它将通过 CoroutineScope.coroutineContext 来承袭上下文，
 * 并且这个新协程的 Job 将会成为父协程作业的 子 作业。
 * todo 当一个父协程被取消的时候，所有它的子协程也会被递归的取消。
 * todo 然而，当使用 GlobalScope 来启动一个协程时，则新协程的作业没有父作业。 因此它与这个启动的作用域无关且独立运作。
 */
fun `子协程`() = runBlocking {
    // 启动一个协程来处理某种传入请求（request）
    val request = launch {
        // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
        GlobalScope.launch {
            println("job1: I run in GlobalScope and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // 取消请求（request）的执行
    delay(1000) // 延迟一秒钟来看看发生了什么
    println("main: Who has survived request cancellation?")
    println()
}

/**
 * 父协程的职责
 * 一个父协程总是等待所有的子协程执行结束。父协程并不显式的跟踪所有子协程的启动，
 * 并且不必使用 Job.join 在最后的时候等待它们
 */
//request: I'm done and I don't explicitly join my children that are still active
//Coroutine 0 is done
//Coroutine 1 is done
//Coroutine 2 is done
//Now processing of the request is complete
fun `父协程的职责`() = runBlocking {
    // 启动一个协程来处理某种传入请求（request）
    val request = launch {
        repeat(3) { i ->
            launch {
                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    request.join() // 等待请求的完成，包括其所有子协程
    println("Now processing of the request is complete")
}

/**
 * 组合上下文中的元素
 */

fun `组合上下文中的元素`() = runBlocking {
    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }

    val coroutineScope = CoroutineScope(Job() + Dispatchers.IO + CoroutineName("test"))
    val job = coroutineScope.launch {
        println("${coroutineContext[Job]} , $${Thread.currentThread().name}")
        val result = async(Dispatchers.Default) {
            println("${coroutineContext[Job]} , $${Thread.currentThread().name}")
            "OK"
        }
        println("result:${result.await()}")
    }
    job.join()

}

/**
 * 协程作用域
 */
class Activity {
    val mainScope = CoroutineScope(Dispatchers.Default)

    fun destroy() {
        mainScope.cancel()
    }

    fun doSomething() {
        // 在示例中启动了 10 个协程，且每个都工作了不同的时长
        repeat(10) { i ->
            mainScope.launch {
                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                println("Coroutine $i is done")
            }
        }
    }
}

fun `协程作用域`() = runBlocking {
    val activity = Activity()
    activity.doSomething() // 运行测试函数
    println("Launched coroutines")
    delay(500L) // 延迟半秒钟
    println("Destroying activity!")
    activity.destroy() // 取消所有的协程
    delay(1000) // 为了在视觉上确认它们没有工作
}

/**
 * 构造器
 * coroutineScope 挂起
 * 多个子协程，如果 有子协程抛出异常，协程全部取消运行
 */

fun `coroutineScope挂起`() = runBlocking {
    try {
        coroutineScope {

            val job1 = launch {
                delay(400)
                println("job1 finished!!")
            }
            val job2 = async {
                delay(200)
                println("job2 finished!!")
                throw  IllegalArgumentException()
            }
        }
    } catch (e: Exception) {
        println("协程异常：$e")
    }
    println()
}

/**
 * 构造器
 *supervisorScope
 * 多个子协程运行，其中一个协程抛出异常，不影响其他子协程的运行
 */
fun `supervisorScope构造器`() = runBlocking {
    try {
        supervisorScope {
            val job1 = launch {
                delay(400)
                println("job1 finished!!")
            }
            val job2 = launch {
                delay(600)
                println("job2 finished!!")
            }
            val job3 = async {
                delay(200)
                println("job3 finished!!")
                throw  IllegalArgumentException()
            }
        }
    } catch (e: Exception) {
        println("协程异常：$e")
    }
}

/**
 * 协程取消
 * 1 ：取消作用域 会取消它的子协程
 */

fun `取消作用域取消子协程`() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        delay(1000)
        println("job 1")
    }
    val job2 = scope.launch {
        delay(1000)
        println("job 2")
    }
    delay(100)
    scope.cancel()
    delay(2000)
}

/**
 * 协程取消
 * 2 ：被取消的子协程 不会影响其余的兄弟协程
 */
fun `被取消的子协程不会影响其余的兄弟协程`() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        delay(1000)
        println("job 1")
    }
    val job2 = scope.launch {
        delay(1000)
        println("job 2")
    }
    delay(100)
    job2.cancel()
    delay(2000)
}

/**
 *协程取消
 * 协程抛出一个特殊的异常CancellationException来处理取消操作
 * 3 ：CancellationException
 */

fun `CancellationException`() = runBlocking {
    val job = GlobalScope.launch {
        try {
            delay(1000)
            println("job 1")
        } catch (e: CancellationException) {
            println("取消异常：$e")
        }
    }
    delay(100)
//    job.cancel()
//    job.cancel(CancellationException("取消"))
//    delay(2000)
    job.cancelAndJoin()
}

/**
 * CoroutineExceptionHandler 异常捕获
 */

fun `CoroutineExceptionHandler异常捕获`() = runBlocking<Unit> {
    val handler = CoroutineExceptionHandler { _, throwable ->
        println("Caught: $throwable")
    }

    val job = GlobalScope.launch(handler) {
        throw AssertionError()
    }
    val deferred = GlobalScope.async {
        throw ArithmeticException()
    }
    job.join()
    deferred.await()
}

fun `CoroutineExceptionHandler异常捕获2`() = runBlocking<Unit> {
    val handler = CoroutineExceptionHandler { _, throwable ->
        println("Caught: $throwable")
    }
    val scope = CoroutineScope(Job())
    val job = scope.launch(handler) {
         launch {
             throw IllegalAccessError()
         }
    }
    job.join()
}
/**
 * 协程上下文继承
 */
fun `协程上下文继承`() = runBlocking {
    val coroutineScope = CoroutineScope(Job() + Dispatchers.IO + CoroutineName("test"))
    val job = coroutineScope.launch(){
        println("${coroutineContext[Job]} , $${Thread.currentThread().name}")
        val result = async(Dispatchers.Default) {
            println("${coroutineContext[Job]} , $${Thread.currentThread().name}")
            "OK"
        }
        println("result:${result.await()}")
    }
    job.join()
}