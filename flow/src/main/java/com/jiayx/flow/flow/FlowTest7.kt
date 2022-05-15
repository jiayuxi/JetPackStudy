package com.jiayx.flow.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 *Created by yuxi_
on 2022/5/15
todo flow 组合操作符
 */

fun main() {
    combine()
    combineTransform()
    merge()
    flattenConcat()
    flattenMerge()
    flatMapContact()
    flatMapLatest()
    flatMapMerge()
    zip()
}

/**
 * combine ： 组合每个流最新发出的值
 */
private fun `combine`() = runBlocking {
    val flow = flowOf(1, 2, 3).onEach { delay(10) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
    coroutineScope {
        flow.combine(flow2) { i, s -> i.toString() + s }.collect {
            println("combine value : $it")
        }
    }
    println()
}

/**
 * combineTransform : combineTransform 效果上与 combine 相同
 * 唯一区别只是，combine在合并后立即发送数据，而combineTransform会经过转换由用户控制是否发送合并后的值。
 * 顾名思义 combine + transform****
 */
private fun `combineTransform`() = runBlocking {
    val flow = flowOf(1, 2, 3).onEach { delay(10) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
    coroutineScope {
        flow.combineTransform(flow2) { number, string ->
            println("number : $number , string : $string")
//            delay(100)
            emit("$number : $string")
        }.collect {
            println("combineTransform value : $it")
        }
    }
    println()
}

/**
 * merge ： 合并多个流成 一个流。 可以用在 多级缓存加载上
 */
private fun `merge`() = runBlocking {
    val flow = flowOf(1, 2).onEach { delay(10) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
    coroutineScope {
        listOf(flow, flow2).merge().collect {
            println("merge value : $it")
        }
    }
    println()
}

/**
 * flattenConcat ： 以顺序方式将给定的流展开为单个流 ，是Flow<Flow<T>>的扩展函数
 */

private fun `flattenConcat`() = runBlocking {
    val flow = flowOf(flowOf(1, 2, 3), flowOf(4, 5, 6))
    coroutineScope {
        flow.flattenConcat().collect {
            println("flattenConcat value: $it")
        }
    }
    println()
}

/**
 * flattenMerge : 作用和 flattenConcat 一样，但是可以设置并发收集流的数量
 * 有个入参：concurrency: Int ,当其 == 1时，效果和 flattenConcat 一样，大于 1 时，则是并发收集。
 */

private fun `flattenMerge`() = runBlocking {
    val flow = flowOf(
        flowOf(1, 2, 3).flowOn(Dispatchers.IO),
        flowOf(4, 5, 6).flowOn(Dispatchers.IO),
        flowOf(7, 8, 9).flowOn(Dispatchers.IO)
    )

    coroutineScope {
        flow.flattenMerge(3).collect {
            println("flattenMerge value : $it")
        }
    }
    println()
}

/**
 * flatMapContact ： 这是一个组合操作符，相当于 map + flattenConcat , 通过 map 转成一个流，在通过 flattenConcat
 * 展开合并成一个流
 */
private fun requestFlow(i: Int) = flow<String> {
    emit("$i: First")
    delay(500)
    emit("$i: Second")
}

private fun `flatMapContact`() = runBlocking {
    val flow = flowOf(1, 2)

    coroutineScope {
        flow.flatMapConcat {
            requestFlow(it)
        }.collect {
            println("flatMapContact value: $it")
        }
    }
    println()
}

/**
 * flatMapLatest ： 和其他 带 Latest的操作符 一样，如果下个值来了，上变换还没结束，就取消掉。
 * 相当于 transformLatest + emitAll
 */

private fun `flatMapLatest`() = runBlocking {
    val flow = flow {
        emit(1)
        delay(100)
        emit(2)
    }

    coroutineScope {
        flow.flatMapLatest {
            flow {
                emit(it)
                delay(200)
                emit("$it _last")
            }
        }.collect {
            println("flatMapLatest value : $it")
        }
    }
    println()
}

/**
 * flatMapMerge :也是组合操作符，简化使用。 map + flattenMerge 。
 * 因此也是有 concurrency: Int 这样一个参数，来限制并发数。
 * 另一种展平模式是并发收集所有传入的流，并将它们的值合并到一个单独的流，以便尽快的发射值
 */

private fun `flatMapMerge`() = runBlocking {
    val flow = flowOf("a", "b", "c", "d", "e", "f")

    coroutineScope {
        flow.flatMapMerge(5) {
            flow {
                emit(it)
            }.flowOn(Dispatchers.IO)
        }.collect {
            print("$it ")
        }
    }
    println()
}

/**
 * zip ： 对两个流进行组合，分别从二者取值，一旦一个流结束了，那整个过程就结束了
 */

private fun `zip`() = runBlocking {
    val flow = flowOf(1, 2, 3).onEach { delay(10) }
    val flow2 = flowOf("a", "b", "c", "d").onEach { delay(15) }

    coroutineScope {
        flow.zip(flow2) { value1, value2 -> "$value1 - $value2" }
    }.collect {
        println("zip value : $it")
    }
    println()
}