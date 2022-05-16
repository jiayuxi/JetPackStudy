package com.jiayx.flow.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*

/**
 *Created by yuxi_
on 2022/5/15
todo flow 变换操作符
 */
fun main() {
    map()
    mapLatest()
    mapNotNull()
    transForm()
    transformLatest()
    transformWhile()
    asStateFlow()
    asSharedFlow()
    receiveAsFlow()
    consumeAsFlow()
    withIndex()
    scan()
    produceIn()
    runningFold()
    runningReduce()
    shareIn()
    stateIn()
}

/**
 * map : 将发出的值 进行变换 ，lambda的返回值为最终发送的值
 */
private fun `map`() = runBlocking {
    val flow = flowOf(1, 2, 3)
    coroutineScope {
        flow.map {
            it * it
        }.collect {
            println("变换操作符 map value：$it")
        }
    }
    println()
}

/**
 * mapLatest: 类比 collectLatest ,当有新值发送时如果上个变换还没结束，会先取消掉
 */

private fun `mapLatest`() = runBlocking {
    val flow = flow {
        emit("a")
        delay(100)
        emit("b")
    }

    coroutineScope {
        flow.mapLatest { value ->
            println("Started computing $value")
            delay(200)
            "Computed $value"
        }.collect {
            println("mapLatest value: $it")
        }
    }
    println()
}

/**
 * mapNotNull : 仅发送 map后不为空的值
 */

private fun `mapNotNull`() = runBlocking {
    val flow = flowOf("a", null, "b")
    coroutineScope {
        flow.mapNotNull {
            println("Started computing : $it")
            it
        }.collect {
            println("mapNotNull value: $it")
        }
    }
    println()
}

/**
 * transform ： 对发出的值 进行变换 。区别于map， transform的接收者是FlowCollector ，
 * 因此它非常灵活，可以变换、跳过它或多次发送。
 */

private fun `transForm`() = runBlocking {
    val flow = flowOf(1, 2, 6, 4)
    coroutineScope {
        flow.filter { it % 2 == 0 }.transform { value ->
            if (value == 2) {
                emit("value:$value * 2")
            }
            emit("transForm:$value")
        }.collect {
            println(it)
        }
    }
    println()
}

/**
 * transformLatest : 类比 mapLatest ,当有新值发送时如果上个变换还没结束，会先取消掉
 */

private fun `transformLatest`() = runBlocking {
    val flow = flow {
        emit("a")
        delay(100)
        emit("b")
    }
    coroutineScope {
        flow.transformLatest { value ->
            emit(value)
            delay(200)
            emit("$value _last")
        }.collect {
            println("transformLatest value: $it")
        }
    }
    println()
}

/**
 * transformWhile :这个变化的lambda 的返回值是 Boolean ,如果为 False则不再进行后续变换, 为 True则继续执行
 */

private fun `transformWhile`() = runBlocking {
    val flow = flowOf(1, 2, 3, 4, 5, 6)

    coroutineScope {
        flow.transformWhile { value ->
            emit(value)
            value % 2 == 0
        }.collect {
            println("transformWhile value: $it")
        }
    }
    println()
}

/**
 * asStateFlow : 将 MutableStateFlow 转换为 StateFlow ，就是变成不可变的。常用在对外暴露属性时使用
 */
private fun `asStateFlow`() = runBlocking {
    val _uiState = MutableStateFlow<Int>(0)
    val uiState = _uiState.asStateFlow()
    (1..2).forEach {
        _uiState.emit(it)
    }
    val job = launch(Dispatchers.IO) {
        uiState.collectLatest {
            println("stateFlow value : $it")
        }
    }
    delay(200)
    job.cancel()
    println()
}

/**
 * asSharedFlow ： 将 MutableSharedFlow 转换为 SharedFlow ，就是变成不可变的。常用在对外暴露属性时使用
 */

private fun `asSharedFlow`() = runBlocking {
    val _uiSharedFlow = MutableSharedFlow<Int>()
    val uiSharedFlow = _uiSharedFlow.asSharedFlow()

    launch(Dispatchers.IO) {
        (1..3).forEach {
            delay(10)
            println("value : $it")
            _uiSharedFlow.emit(it)
        }
    }
    val job = launch(Dispatchers.IO) {
        uiSharedFlow.collectLatest {
            println("sharedFlow value : $it")
        }
    }
    delay(1000)
    job.cancel()
    println()
}

/**
 * receiveAsFlow : 将Channel 转换为Flow ,可以有多个观察者，但不是多播，可能会轮流收到值。
 */

private fun `receiveAsFlow`() = runBlocking {

}

/**
 * consumeAsFlow ：将Channel 转换为Flow ,但不能多个观察者（会crash）!
 */
private fun `consumeAsFlow`() = runBlocking {

}

/**
 * withIndex : 将结果包装成IndexedValue 类型
 */

private fun `withIndex`() = runBlocking {
    val flow = flowOf("a", "b")

    coroutineScope {
        flow.withIndex().collect {
            println("withIndex value: ${it.index} : ${it.value}")
        }
    }
    println()
}

/**
 * scan : 和 fold 相似，区别是fold 返回的是最终结果，scan返回的是个flow ，会把初始值和每一步的操作结果发送出去。
 */

private fun `scan`() = runBlocking {
    val flow = flowOf(1, 2, 3)
    coroutineScope {
        flow.scan(0) { result, value ->
            result + value
        }.collect {
            println("scan result: $it")
        }
    }
    //// 0 1 3 6
    //acc 是上一步操作的结果， value 是发射的值
    //0 是 初始值
    //1 是 0 + 1 = 1
    //3 是 1 + 2 = 3
    //6 是 3 + 3 = 6
    println()
}

/**
 * produceIn : 转换为 ReceiveChannel , 不常用。
 * 注： Channel 内部有 ReceiveChannel 和 SendChannel之分,看名字就是一个发送，一个接收。
 */

private fun `produceIn`() = runBlocking {
    flowOf(1, 2, 3).produceIn(this).consumeEach {
        println("produceIn value: $it")
    }
    println()
}

/**
 * runningFold : 区别于 fold ，就是返回一个新流，将每步的结果发射出去
 */

private fun `runningFold`() = runBlocking {
    val flow = flowOf(1, 2, 3)
    coroutineScope {
        flow.runningFold(0) { result, value ->
            result + value
        }.collect {
            println("runningFold value : $it")
        }
    }
    //结果
    //runningFold value : 0
    //runningFold value : 1
    //runningFold value : 3
    //runningFold value : 6
    println()
}

/**
 * runningReduce ： 区别于 reduce ，就是返回一个新流，将每步的结果发射出去。
 */

private fun `runningReduce`() = runBlocking {
    val flow = flowOf(1, 2, 3)
    coroutineScope {
        flow.runningReduce { accumulator, value ->
            accumulator + value
        }.collect {
            println("runningReduce value : $it")
        }
    }
    //结果
    //runningReduce value : 1
    //runningReduce value : 3
    //runningReduce value : 6
    println()
}

/**
 * shareIn : 将普通flow 转化为 SharedFlow ,
 * 其有三个参数:
 * scope: CoroutineScope 开始共享的协程范围
 * started: SharingStarted 控制何时开始和停止共享的策略
 * replay: Int = 0 发给 新的订阅者 的旧值数量
 * 其中 started 有一些可选项:
 * 1.Lazily: 当首个订阅者出现时开始，在scope指定的作用域被结束时终止。
 * 2.Eagerly: 立即开始，而在scope指定的作用域被结束时终止。
 * WhileSubscribed : 在第一个订阅者出现时开始共享，在最后一个订阅者消失时立即停止（默认情况下），永久保留重播缓存（默认情况下）
 * WhileSubscribed 具有以下可选参数：
 * stopTimeoutMillis — 配置最后一个订阅者消失到协程停止共享之间的延迟（以毫秒为单位）。 默认为零（立即停止）。
 * replayExpirationMillis - 共享的协程从停止到重新激活，这期间缓存的时效
 */
private fun `shareIn`() = runBlocking {
    val sharedFlow = flowOf(1, 2, 3).onEach { delay(200) }.shareIn(this, SharingStarted.Eagerly)

    val job = launch(Dispatchers.IO) {
        //可以有多个噶观察者
        sharedFlow.collect {
            println("shareIn value: $it")
        }
    }
    delay(2000)
    job.cancelAndJoin()
    println()
}

/**
 * stateIn : 将普通flow 转化为 StateFlow 。
 * 其有三个参数：
 * scope - 开始共享的协程范围
 * started - 控制何时开始和停止共享的策略
 * initialValue - 状态流的初始值
 */

private fun `stateIn`() = runBlocking {
    val state = flowOf(1, 2, 3).stateIn(this, SharingStarted.Eagerly, 0)
    val job = launch(Dispatchers.IO) {
        //可以有多个噶观察者
        state.collect {
            println("stateIn value: $it")
        }
    }
    delay(1000)
    job.cancelAndJoin()
}