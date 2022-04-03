package com.jiayx.flow.select

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/**
 *Created by yuxi_
on 2022/4/3
select 表达式
 */

fun main() {
  select语句()
}

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

suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
    select<Unit> { //<Unit> 意味着该 select 表达式不返回任何结果
        fizz.onReceive { value -> // 第一个 select 子句
            println("fizz -> $value")
        }
        buzz.onReceive { value -> // 第二个select 子句
            println("buzz -> $value")
        }
    }
}

fun `select语句`() = runBlocking {
    val fizz = fizz()
    val buzz = buzz()
    repeat(7){
        selectFizzBuzz(fizz,buzz)
    }
    coroutineContext.cancelChildren() // 取消 fizz 和 buzz 协程
}