package com.jiayx.flow.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 *Created by yuxi_
on 2022/5/15
TODO flow 末端操作符
 */

fun main() {
    末端操作符()
    toCollection将结果添加到集合()
    toListOrToSet()
    launchIn()
    lastOrLastOrNull()
    firstOrFirstOrNull()
    single只接收一个值而且不能为空()
    count返回流发送值的个数()
    foldOrReduce()
}

/**
 *  todo 末端操作符
 *  collect : 触发flow的运行 。 通常的监听方式
 *  collectLatest :与 collect的区别是 ，有新值发出时，如果此时上个收集尚未完成，则会取消掉上个值的收集操作
 *  collectIndex : 带下标的 收集操作
 *  toCollection : 将结果添加到集合
 */

fun `末端操作符`() = runBlocking {
    val flow = flowOf(1, 2, 3)
    coroutineScope {
        // 末端操作符
        flow.collectLatest { }
        flow.collect {}
        flow.collectIndexed { index, value ->
            println("末端操作 collectIndex:index:$index,value:$value")
        }
    }
    println()
}

/**
 * toCollection 將結果添加到集合
 */

fun `toCollection将结果添加到集合`() = runBlocking {
    val arrayList = arrayListOf(0)
    coroutineScope {
        flow {
            emit(1)
            emit(2)
        }.toCollection(arrayList)
    }
    arrayList.forEach {
        println("toCollect 将结果添加到集合：$it")
    }
    println()
}

/**
 *  toList,toSet
 *  将结果转化为 list，set 集合
 */
fun `toListOrToSet`() = runBlocking {
    flow {
        emit(1)
        emit(2)
        emit(2)
    }.toList().forEach {
        print("toList:$it ")
    }
    println()
    flow {
        emit(4)
        emit(5)
        emit(5)
    }.toSet().forEach {
        print("toSet value:$it ")
    }
    println()
}

/**
 * launchIn
 * 直接触发流的执行，不设置action,入参为coroutineScope 一般不会直接调用，
 * 会搭配别的操作符一起使用，如onEach,onCompletion 。返回值是Job
 * 使用 launchIn 替换 collect 我们可以在单独的协程中启动流的收集，
 * 这样就可以立即继续进一步执行代码
 * 返回一个 Job 对象，可以取消运行
 */

fun `launchIn`() = runBlocking {
    flow {
        emit(1)
        emit(2)
    }.onEach { println("launchIn value:$it") }
        .launchIn(this)// <--- 在单独的协程中执行流

    println()
}

/**
 *  last : 返回流 发出 的最后一个值 ,如果flow 为空会抛异常
 *  lastOrNull: 返回流 发出 的最后一个值 ,可以为空
 */

fun `lastOrLastOrNull`() = runBlocking {
    val flow = flowOf<Int>(1, 2, 3)
    val flow2 = flowOf("张三", "李四", null)
    val flow3 = emptyFlow<Int>()
    coroutineScope {
        println("获取最后一个值last value: ${flow.last()}")
        println("获取最后一个值 lastOrNull value: ${flow.lastOrNull()}")
        println()
        println("获取最后一个值last value: ${flow2.last()}")
        println("获取最后一个值 lastOrNull value: ${flow2.lastOrNull()}")
        println()
        // 如果 flow 为空，调用 last 为抛出异常
//        println("获取最后一个值last value: ${flow3.last()}")
        println("获取最后一个值 lastOrNull value: ${flow3.lastOrNull()}")
    }
    println()
}

/**
 * first ：返回流 发出 的第一个值 ,如果flow为空会抛异常
 * firstOrNull: 返回流 发出 的第一个值 ,可以为空
 */

fun `firstOrFirstOrNull`() = runBlocking {
    val flow1 = flowOf(1, 2)
    val flow2 = flowOf<Int>()
    coroutineScope {
        println("first value: ${flow1.first()}")
        println("firstOrNull value: ${flow2.firstOrNull()}")
    }
    println()
}

/**
 * single ：接收流发送的第一个值 ，区别于first(),如果为空或者发了不止一个值，则都会报错
 * singleOrNull: 接收流发送的第一个值 ，可以为空 ,发出多值的话除第一个，后面均被置为null
 */

fun `single只接收一个值而且不能为空`() = runBlocking {
    val flow = flowOf(1)
    val flow2 = flowOf(1, 2, 3)
    val flow3 = emptyFlow<Int>()
    coroutineScope {
        println("single value: ${flow.single()}")
        println("singleOrNull value: ${flow.singleOrNull()}")
        println("singleOrNull value: ${flow2.singleOrNull()}")
        println("singleOrNull value: ${flow3.singleOrNull()}")
    }
    println()
}

/**
 * count : 返回流发送值的个数。 类似 list.size() ，注：sharedFlow无效(无意义）
 */


fun `count返回流发送值的个数`() = runBlocking {
    val flow = flow {
        emit(2)
        emit(3)
    }
    coroutineScope {
        println("流发送值的个数：${flow.count()}")
    }
    println()
}

/**
 * fold : 从初始值开始 执行遍历,并将结果作为下个执行的 参数。
 * reduce : 和fold 差不多。 无初始值
 */
fun `foldOrReduce`() = runBlocking {
    // fold
    val sum = flowOf(1, 2, 3).fold(1) { result, value ->
        result + value
    }
    println("fold value : $sum")
    // reduce
    val result = flowOf(1, 2, 3, 4).reduce { accumulator, value ->
        accumulator * value
    }
    println("reduce value: $result")
    println()
}