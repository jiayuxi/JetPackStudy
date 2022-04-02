package com.jiayx.flow.flow

import android.util.ArrayMap
import androidx.collection.arrayMapOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

/**
 *Created by yuxi_
on 2022/4/1
 */
/**
 * 状态操作符
状态操作符不做任何修改，只是在合适的节点返回状态。
onStart：在上游生产数据前调用

onCompletion：在流完成或者取消时调用

onEach：在上游每次emit前调用

onEmpty：流中未产生任何数据时调用

catch：对上游中的异常进行捕获

retry、retryWhen：在发生异常时进行重试，retryWhen中可以拿到异常和当前重试的次数
 */

fun main() {
//    testFlow()
//    map变换操作符()
//    mapNotNull变换操作符()
//    mapLatest变换操作符()
//    flatMapConcat变换操作符()
//    flatMapMerge并发操作变换符()
//    transform转换操作符()
//    flatMapLatest处理最新值()
//    take限长操作符()
//    flowDecode过滤高频发的生产数据()
    flowDistinctUntilChanged过滤上游重复的元素()
//    flowOn线程调度()
//    flowOnStartEvent事件转换操作符()
//    onEachEvent事件操作符()
//    flowLaunchIn末端操作符()
//    onEmptyEvent操作符事件()
//    命令式finally完成()
//    声明式处理onCompletion()
//    onCompletion判断是正常完成还是异常完成()
//    onCompletion成功完成()
//      flowZip组合流()
//    tryCatch异常捕获()
//    catch异常捕获()
//    声明式捕获异常()
//    combine合并操作符()
//    asFlow扩展函数()
//    FlowFilter过滤操作符()
//    flowOn更改流发射的上下文操作()
//    flowBuffer缓冲操作符()
//    flowConflate合并操作符()
//      flowCollectLatest处理最新值()
}

/**
 * 末端流操作符
 */
fun `testFlow`() = runBlocking {
    val scope = CoroutineScope(SupervisorJob())
    val flow = flow {
        (1..3).forEach {
            emit(it)
        }
    }
    scope.launch {
        println("获取第一个：${flow.first()}")
        println("获取最后一个: ${flow.last()}")
        println("转化为集合: ${flow.toList()}")
        println("转化为set集合: ${flow.toSet()}")
        val sum = flow.map {
            it * it
        }.reduce { accumulator, value -> //求和
            accumulator + value
        }
        println("末端操作符reduce求和 ：$sum")
        flow.collect {
            //消费数据
            println("last collect result ：$it")
        }
    }.join()
    val map = arrayMapOf("key1" to 1, Pair("key2", 2))
    println()
}

/**
 * map 变换操作符
 */
fun `map变换操作符`() = runBlocking {
    val flow = flow {
        (1..5).forEach {
            emit(it)
        }
    }.map {
        println("running map ：$it")
        it * 2 + 1
    }.collect {
        println("last collect result: $it")
    }
    println()
}

/**
 * mapLatest
 */
fun `mapLatest变换操作符`() = runBlocking {
    (1..2).asFlow().onEach { delay(100) }
        .mapLatest {
            println("数值：value: $it")
            "变换后的数值 ：$it"
        }.collect {
            println("last result value : $it")
        }
    println()
}

/**
 *
 */
fun `mapNotNull变换操作符`() = runBlocking {
    flowOf("1", null, "2").mapNotNull {
        it
    }.collect {
        println("last result value : $it")
    }
    println()
}

/**
 * flatMapConcat 链接模式操作符
 * 它们在等待内部流完成之前开始收集下一个值
 */
fun `flatMapConcat变换操作符`() = runBlocking {
    flow {
        (1..2).forEach {
            emit(it)
        }
    }.flatMapConcat { index ->
        flow<String> {
            println("running flatMapConcat first $index")
            emit("flatMapConcat first $index")
            println("running flatMapConcat second $index")
            emit("flatMapConcat second $index")
            println("running flatMapConcat end $index")
        }
    }.collect {
        println("last collect result ：$it")
    }
    println()
}

/**
 * flatMapMerge 并发操作符
 * 另一种展平模式是并发收集所有传入的流，并将它们的值合并到一个单独的流，以便尽快的发射值
 */
fun `flatMapMerge并发操作变换符`() = runBlocking {
    flow {
        (1..3).forEach {
            println("flatMapMerge 1: $it")
            emit(it)
        }
    }.flatMapMerge { index ->
        flow<String> {
            emit("flatMapConcat $index ,1")
            emit("flatMapConcat $index ,2")
        }
    }.collect {
        println("last collect result ：$it")
    }
    println()
}

/**
 * flatMapLatest
 * 与 collectLatest 操作符类似,也有相对应的“最新”展平模式，在发出新流后立即取消先前流的收集
 */
fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // 等待 500 毫秒
    emit("$i: Second")
}

/**
 * flatMapLatest
 */
fun `flatMapLatest处理最新值`() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis() // 记录开始时间
    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
        .flatMapLatest { requestFlow(it) }
        .collect { value -> // 收集并打印
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/**
 * 转换操作符
 * transform
 */

fun `transform转换操作符`() = runBlocking {
    (1..6).asFlow()// 一个请求流
        .filter { it % 2 == 0 }
        .transform { request ->
            emit("Making request $request")
            emit(performRequest(request))
        }
        .collect { response -> println(response) }
    println()
}

/**
 * take
 * 中间操作符
 * 限长操作符
 */
fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

fun `take限长操作符`() = runBlocking {
    numbers().take(2).collect {
        println("last result value : $it")
    }
    println()
}

/**
 *  中间操作符
 *  decode
 *  中间操作符，在指定时间内，只允许最新的值传递到下游，多用于过滤上游高频率的生产者数据。
 */
fun `flowDecode过滤高频发的生产数据`() = runBlocking {
    (1..3).asFlow().onEach { delay(100) }.debounce(200).collect {
        println("last result 过滤高频发的数据值value : $it")
    }
}

/**
 * 中间操作符
 * distinctUntilChanged
 *
 * 中间操作符，过滤上游数据流中的重复元素
 */
fun `flowDistinctUntilChanged过滤上游重复的元素`() = runBlocking {
    flowOf("wang", "lisi", "zhangsan", "zhangsan").distinctUntilChanged().collect {
        println("last result 过滤上游重复的元素：$it")
    }
}

suspend fun performRequest(request: Int): String {
    delay(1000) // 模仿长时间运行的异步任务
    return "response $request"
}

/**
 * 调度操作符
 */
fun `flowOn线程调度`() = runBlocking {
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

/**
 * onStart 在上游数据流开始之前执行操作
 * 如果onStart内发送数据，会先将onStart数据发送传递到下游消费完成后，才开始将上游数据流传递到下游。
 *  onStart 事件开始
last result: 0
onStart 事件结束
last result: 1
last result: 2
 */
fun `flowOnStartEvent事件转换操作符`() = runBlocking {
    // onStart
    flow {
        (1..2).forEach {
            emit(it)
        }
    }.onStart {
        println("onStart 事件开始")
        delay(500)
        emit(0)
        println("onStart 事件结束")
    }.collect {
        println("last result: $it")
    }
    println()
}

/**
 * onEach
 * 上游数据流的每个值发送前执行操作
开始发送数据：value: 1
last result: 1
开始发送数据：value: 2
last result: 2
 */
// 模仿事件流
fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }
fun `onEachEvent事件操作符`() = runBlocking {
    events().onEach {
        println("开始发送数据：value: $it")
    }.collect {
        println("last result: $it")
    }
    println()
}

/**
 * onEmpty
 * 在上游数据流完成时，没有任何值传递消费，则触发执行操作，可以发送额外的元素
 */
fun `onEmptyEvent操作符事件`() = runBlocking {
    flow {
        (1..2).forEach {
            emit(it)
        }
    }.onEmpty {
        println("发送结束")
//        emit(100)
    }.collect {
        println("last result: $it")
    }
    println()
}

/**
 * launchIn
 * 末端操作符可以在这里派上用场。
 * 使用 launchIn 替换 collect 我们可以在单独的协程中启动流的收集，
 * 这样就可以立即继续进一步执行代码
 */
fun `flowLaunchIn末端操作符`() = runBlocking {
    events()
        .onEach { event -> println("Event: $event") }
        .launchIn(this) // <--- 在单独的协程中执行流
    println("Done")
}
/**
 * flow 的完成
 * 当流收集完成时（普通情况或异常情况），它可能需要执行一个动作。
 * 你可能已经注意到，它可以通过两种方式完成：命令式或声明式。
 */
/**
 * 命令是 finally 块
 */
fun `命令式finally完成`() = runBlocking {
    try {
        (1..2).asFlow().collect { value -> println(value) }
    } finally {
        println("Done")
    }
    println()
}

/**
 * 声明式 处理
 * 对于声明式，流拥有 onCompletion 过渡操作符，它在流完全收集时调用。
 */

fun `声明式处理onCompletion`() = runBlocking {
    (1..2).asFlow().onCompletion {
        println("Done")
    }.collect {
        println("last result value : $it")
    }
}

/**
 * onCompletion
 * 的主要优点是其 lambda 表达式的可空参数
 * Throwable 可以用于确定流收集是正常完成还是有异常发生
 * onCompletion 操作符与 catch 不同，它不处理异常。我们可以看到前面的示例代码，异常仍然流向下游。
 * 它将被提供给后面的 onCompletion 操作符，并可以由 catch 操作符处理
 */
fun simpleOnCompletion(): Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}

fun `onCompletion判断是正常完成还是异常完成`() = runBlocking {
    simpleOnCompletion().onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
        .catch { cause -> println("Caught exception") }
        .collect { value -> println(value) }
    println()
}

/**
 *  成功完成
 *  与 catch 操作符的另一个不同点是 onCompletion 能观察到所有异常并且仅在上游流成功完成（
 *  没有取消或失败）的情况下接收一个 null 异常。
 */
fun `onCompletion成功完成`() = runBlocking {
    (1..2).asFlow().onCompletion { cause ->
        println("Flow completed with $cause")
    }.catch { e -> println("Caught : $e") }
        .collect { value ->
            println("last result value : $value")
        }
    println()
}

/**
 * 异常捕获
 * try catch
 * 捕获了在发射器或任何过渡或末端操作符中发生的任何异常
 *
 */

fun `tryCatch异常捕获`() = runBlocking {
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
fun `catch异常捕获`() = runBlocking {
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
fun `声明式捕获异常`() = runBlocking {
    (1..3).asFlow()
        .onEach { value ->
            check(value <= 1) { "Collected $value" }
            println("value: $value")
        }
        .catch { e -> println("Caught $e") }
        .collect()
}

/**
 * zip 组合流
 */
fun `flowZip组合流`() = runBlocking {
    val nums = (1..3).asFlow()
    val strs = flowOf("first", "two", "three", "four")
    strs.zip(nums) { a, b ->
        "$a -> $b"
    }.collect {
        println("last result value: $it")
    }
}

/**
 * combine 合并操作符
 *flow {} 最基本的构建器
 * flowOf 流的构建器定义了一个发射固定值集的流
 */

fun `combine合并操作符`() = runBlocking {
    val flow1 = flowOf("first", "second", "third").onEach { delay(50) }
    val flow2 = flowOf(1, 2, 3).onEach { delay(100) }

    flow1.combine(flow2) { first, second ->
        "$first $second"
    }.collect {
        println("last result: $it")
    }
    //flow2元素的1元素发送时，flow1数据源的second元素是最新值，
    //所以first会被忽略
    //last result: first 1
    //last result: second 1
    //last result: third 1
    //last result: third 2
    //last result: third 3
    println()
}

/**
 * combineTransform 效果上与 combine 相同
 * 唯一区别只是，combine在合并后立即发送数据，而combineTransform会经过转换由用户控制是否发送合并后的值。
 */

fun `combineTransform操作符`() = runBlocking {
    val flow1 = flowOf("first", "second", "third").onEach { delay(50) }
    val flow2 = flowOf(1, 2, 3).onEach { delay(100) }

    println()
}

/**
 * 使用 .asFlow() 扩展函数，可以将各种集合与序列转换为流
 */
fun `asFlow扩展函数`() = runBlocking {
    //集合转化为流
    (1..3).asFlow().collect {
        println("集合扩展函数：$it")
    }
    println()
    // 挂起函数转化为流
    simple1().asFlow().collect {
        println("挂起函数扩展函数: $it")
    }
    println()
    //序列转化为流
    simple2().asFlow().collect {
        println("序列函数扩展函数：$it")
    }
    println()
    // 迭代器转化为流
    val map = mapOf(Pair("key1", 1), Pair("key2", 2))
    map.entries.asFlow().collect {
        println("迭代器函数扩展函数：${it.key},${it.value}")
    }
    println()
}

/**
 * 挂起函数
 */
suspend fun simple1(): List<Int> {
    delay(1000) // 假装我们在这里做了一些异步的事情
    return listOf(1, 2, 3)
}

/**
 * 序列函数
 */
fun simple2(): Sequence<Int> = sequence { // 序列构建器
    for (i in 1..3) {
        Thread.sleep(100) // 假装我们正在计算
        yield(i) // 产生下一个值
    }
}

/**
 * filter 过滤操作符
 */
fun `FlowFilter过滤操作符`() = runBlocking {
    (1..5).asFlow()
        .filter {
            println("Filter : $it")
            it % 2 == 0
        }.map {
            println("Map: $it")
            "string $it"
        }.collect {
            println("last result value : $it")
        }
}
/**
 * withContext 用于在 Kotlin 协程中改变代码的上下文
 * 但是 flow {...} 构建器中的代码必须遵循上下文保存属性，并且不允许从其他上下文中发射（emit）
 */
/**
 * flowOn 该函数用于更改流发射的上下文
 */
fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
        log("Emitting $i")
        emit(i) // 发射下一个值
    }
}.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式

fun `flowOn更改流发射的上下文操作`() = runBlocking {
    simple().collect { value ->
        log("Collected $value")
    }
    println()
}

/**
 * buffer 缓冲操作符
 */
fun simpleBuffer(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
}.buffer().flowOn(Dispatchers.IO)


fun `flowBuffer缓冲操作符`() = runBlocking {
    val time = measureTimeMillis {
        simpleBuffer().buffer().collect {
            delay(300)
            println("value: $it")
        }
    }
    println("消耗的时间：$time")
}

/**
 *  conflate
 *  当收集器处理它们太慢的时候， conflate 操作符可以用于跳过中间值
 */

fun simpleConflate(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
}

/**
 * 我们看到，虽然第一个数字仍在处理中，
 * 但第二个和第三个数字已经产生，因此第二个是 conflated ，
 * 只有最新的（第三个）被交付给收集器：
 * value: 1
 * value: 3
 * 消耗的时间：801
 */
fun `flowConflate合并操作符`() = runBlocking {
    val time = measureTimeMillis {
        simpleBuffer().conflate().collect {
            delay(300)
            println("value: $it")
        }
    }
    println("消耗的时间：$time")
}

/**
 * collectLatest
 * 处理最新值
 */
fun `flowCollectLatest处理最新值`() = runBlocking {
    val time = measureTimeMillis {
        simpleBuffer().collectLatest { value ->
            println("Collecting $value")
            delay(300) // 假装我们花费 300 毫秒来处理它
            println("Done $value")
        }
    }
    println("消耗的时间：$time")
}

/**
 * retry、retryWhen：在发生异常时进行重试，retryWhen中可以拿到异常和当前重试的次数
 */
fun simpleRety(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
    throw Exception("抛出异常")
}

fun `retry发生异常时重试`() = runBlocking {

}

/**
 * 操作符复用
 */

fun <T> Flow<T>.preHandleHttpResponse(dispatcher: CoroutineDispatcher = Dispatchers.IO) {

}


