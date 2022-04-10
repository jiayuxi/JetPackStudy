package com.jiayx.flow.select

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.selects.select

/**
 *Created by yuxi_
on 2022/4/3
select 表达式
 */

fun main() {
//    await多路复用()
//    channel复用多个channel()
//    flow实现多路复用()
    await函数的多路复用()
}

/**
 * 创建 发送者
 */
fun CoroutineScope.fizz() = produce<String> {
    while (true) {
        delay(300)
        send("Fizz")
    }
}

fun CoroutineScope.buzz() = produce<String> {
    while (true) {
        delay(500)
        send("Buzz")
    }
}

/**
 * 返回最快的 那个值
 */
suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
    withContext(Dispatchers.IO) {
        select<Unit> { //<Unit> 意味着该 select 表达式不返回任何结果
            fizz.onReceive { value -> // 第一个 select 子句
                println("fizz -> $value")
            }
            buzz.onReceive { value -> // 第二个select 子句
                println("buzz -> $value")
            }
        }
    }
}

/**
 * 复用多个 channel
 */
fun `channel复用多个channel`() = runBlocking {
    val fizz = fizz()
    val buzz = buzz()
    repeat(7) {
        selectFizzBuzz(fizz, buzz)
    }
    coroutineContext.cancelChildren() // 取消 fizz 和 buzz 协程

    println()
}

/**
 * await 选择表达式
 * select
 * 其允许同时等待多个挂起的结果，并且只取用其中最快完成的作为函数恢复的值。
 */

fun `await多路复用`() = runBlocking {
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
 * 使用 flow 实现多路复用
 */
fun `flow实现多路复用`() = runBlocking {
    val name = "guest"
    coroutineScope {
        listOf(fizz(), buzz())
            .map { function -> // 遍历调用
                function.receive()
            }.map { deferred -> //
                flow {
                    emit(deferred)
                }
            }.merge() // 多个flow合并成一个flow
            .collect { data -> // 末端操作符
                println("Result: $data")
                println("collect")
            }
    }
}

/**
 * await 多路复用
 */

fun CoroutineScope.getLocationFunction() = async {
    delay(1000)
    666
}

fun CoroutineScope.getServiceFunction() = async {
    delay(1200)
    888
}

fun `await函数的多路复用`() = runBlocking {
    GlobalScope.launch {
        val locationFunction = getLocationFunction()
        val serviceFunction = getServiceFunction()
        val result = select<Int>{
            locationFunction.onAwait{ data ->
                data
            }
            serviceFunction.onAwait{ data ->
                data
            }
        }
        println("返回的结果值 ：$result")
    }.join()
}