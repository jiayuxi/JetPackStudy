package com.jiayx.flow.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *Created by yuxi_
on 2022/5/15
todo flow 回调操作符
 */

fun main() {
    onStart回调操作符()
    onCompletion()
    onEach()
    onEmpty()
    onSubscription()
}

/**
 * TODO 回调操作符
 * onStart ： 在上游流开始之前被调用。 可以发出额外元素,也可以处理其他事情，比如发埋点
 */
/*
onStart 事件开始
last result: 0
onStart 事件结束
last result: 1
last result: 2
*/
fun `onStart回调操作符`() = runBlocking {
    val flow = flow {
        (1..2).forEach {
            emit(it)
        }
    }
    coroutineScope {
        flow.onStart {
            println("onStart 事件开始")
            delay(500)
            emit(0)
            println("onStart 事件结束")
        }.collect {
            println("last result: $it")
        }
    }
    // 执行结果
    //onStart 事件开始
    //last result: 0
    //onStart 事件结束
    //last result: 1
    //last result: 2
    println()
}

/**
 * onCompletion : 在流取消或者结束时调用。可以执行发送元素，发埋点等操作
 * 的主要优点是其 lambda 表达式的可空参数
 * Throwable 可以用于确定流收集是正常完成还是有异常发生
 * onCompletion 操作符与 catch 不同，它不处理异常。我们可以看到前面的示例代码，异常仍然流向下游。
 * 它将被提供给后面的 onCompletion 操作符，并可以由 catch 操作符处理
 *
 * 声明式 处理
 * 对于声明式，流拥有 onCompletion 过渡操作符，它在流完全收集时调用。
 *
 */
private fun simpleOnCompletion(): Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}

fun `onCompletion`() = runBlocking {
    val flow = flowOf(1, 2)
    coroutineScope {
        flow.onCompletion {
            println("onCompletion Done")
        }.collect {
            println("onCompletion value:$it")
        }
    }
    println()
    //todo onCompletion判断是正常完成还是异常完成
    simpleOnCompletion().onCompletion { cause ->
        if (cause != null) println("Flow completed exceptionally : $cause") else println(
            "正常结束"
        )
    }
        .catch { cause -> println("Caught exception : $cause") }
        .collect { value -> println(value) }
    println()
    // *  成功完成
    // *  与 catch 操作符的另一个不同点是 onCompletion 能观察到所有异常并且仅在上游流成功完成（
    // *  没有取消或失败）的情况下接收一个 null 异常。
    coroutineScope {
        flow.onCompletion { cause ->
            println("Flow completed with $cause")
        }.catch { e -> println("Caught : $e") }
            .collect { value ->
                println("last result value : $value")
            }
    }
    println()

}

/**
 * onEach: 上游数据流的每个值发送前执行操作
 */
private fun `onEach`() = runBlocking {
    val flow = flowOf(1, 3, 4)
    coroutineScope {
        flow.onEach {
            println("开始发送数据：$it")
        }.collect {
//            println("onEach value:$it")
        }
        println()
        flow.onEach { println("发送数据：$it") }.launchIn(this)
    }
    println()
}

/**
 * onEmpty : 当流完成却没有发出任何元素时回调。 可以用来兜底.
 * 在上游数据流完成时，没有任何值传递消费，则触发执行操作，可以发送额外的元素
 */

private fun `onEmpty`() = runBlocking {
    val flow = flowOf(1, 2, 3)
    val flow2 = emptyFlow<Int>()
    coroutineScope {
        flow.onEmpty {
            println("onEmpty Done")
            emit(100)
        }.collect {
            println("onEmpty value:$it")
        }
        println()
        flow2.onEmpty {
            println("onEmpty 操作结束")
        }.collect {
            println("onEmpty value:$it")
        }
    }
    println()
}

/**
 * onSubscription : SharedFlow 专属操作符 （StateFlow是SharedFlow 的一种特殊实现）
 * 在建立订阅之后 回调。 和 onStart 有些区别 ，SharedFlow 是热流，因此如果在onStart里发送值，则下游可能接收不到。
 */

private fun `onSubscription`() = runBlocking {
    val state = MutableSharedFlow<String>().onSubscription {
        emit("onSubscription")
    }
    launch {
        state.onStart {
            println("接收的数据 value:${this}")
            emit("hello")
        }.collect {
            println(it)
        }
    }
}