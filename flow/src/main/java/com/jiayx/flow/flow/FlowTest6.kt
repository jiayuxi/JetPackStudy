package com.jiayx.flow.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 *Created by yuxi_
on 2022/5/15
todo flow 过滤操作符
 */

fun main() {
    filter()
    filterInstance()
    filterNot()
    filterNotNull()
    drop()
    dropWhile()
    take()
    takeWhile()
    debounce()
    sample()
    distinctUntilChangedBy()
    distinctUntilChanged()
}

/**
 * filter : 筛选出符合条件
 */

private fun `filter`() = runBlocking {
    val flow = flowOf(1, 2, 3, 4, 5, 6)
    val flow2 = flowOf("a", "b", "v")
    coroutineScope {
        flow.filter {
            it % 2 == 0
        }.collect {
            println("filter value : $it")
        }
        println()
        flow2.filter {
            it != "a"
        }.collect {
            println("filter value: $it")
        }
    }
    println()
}

/**
 * filterInstance ： 筛选对应类型的值
 */
private fun `filterInstance`() = runBlocking {
    val flow = flowOf("a", "b", "c", 1, 2)

    coroutineScope {
        flow.filterIsInstance<String>().collect {
            println("filterInstance value: $it")
        }
    }
    println()
}

/**
 * filterNot : 筛选不符合条件相反的值,相当于filter取反
 */

private fun `filterNot`() = runBlocking {
    val flow = flowOf("a", "b", "c")
    coroutineScope {
        flow.filterNot { it != "a" }.collect {
            println("filterNot value : $it")
        }
    }
    println()
}

/**
 * filterNotNull : 筛选不为空的值
 */

private fun `filterNotNull`() = runBlocking {
    val flow = flowOf("a", "v", "c", null)
    coroutineScope {
        flow.filterNotNull().collect {
            println("filterNotNull value : $it")
        }
    }
    println()
}

/**
 * drop : 入参count为int类型 ,作用是 丢弃掉前 n 个的值
 */

private fun `drop`() = runBlocking {
    val flow = flowOf(1, 2, 3, 4, 5, 6)

    coroutineScope {
        flow.drop(3).collect {
            println("drop value : $it")
        }
    }
    println()
}

/**
 * dropWhile : 这个操作符有点特别，和filter 不同！
 * 它是找到第一个不满足条件的，返回当前值和其之后的值。如果首项就不满足条件，则是全部返回。
 */

private fun `dropWhile`() = runBlocking {
    val flow = flowOf(1, 2, 3)

    coroutineScope {
        flow.dropWhile { it == 3 }.collect {
            println("dropWhile value : $it ")
        }
        println()
        flow.dropWhile { it == 1 }.collect {
            println("dropWhile value : $it ")
        }
    }
    println()
}

/**
 * take : 返回前 n个 元素
 */
val flow = flowOf(1, 2, 3, 6)
private fun `take`() = runBlocking {
    coroutineScope {
        flow.take(2).collect {
            println("take value : $it")
        }
    }
    println()
}

/**
 * takeWhile : 也是找第一个不满足条件的项，但是取其之前的值 ，和dropWhile 相反。
 * 如果第一项就不满足，则为空流
 */

private fun `takeWhile`() = runBlocking {
    coroutineScope {
        flow.takeWhile { it < 3 }.collect {
            println("takeWhile value: $it")
        }
        flow.takeWhile { it < 1 }.onEmpty {
            println("onEmpty")
        }.collect {
            println("takeWhile value: $it")
        }
    }
    println()
}

/**
 * debounce ： 防抖节流 ，指定时间内的值只接收最新的一个，其他的过滤掉。搜索联想场景适用
 */
private fun `debounce`() = runBlocking {
    val flow = flow {
        emit(1)
        delay(100)
        emit(2)
        delay(200)
        emit(3)
        delay(1010)
        emit(4)
        delay(1010)
        emit(5)
    }
    coroutineScope {
        flow.debounce(1000).collect {
            println("debounce value : $it")
        }
    }
    println()
}

/**
 * sample : 采样 。给定一个时间周期，仅获取周期内最新发出的值
 */

private fun `sample`() = runBlocking {
    val flow = flow {
        var startTime = System.currentTimeMillis()
        repeat(10) {
            emit(it)
            println("value : $it - time : ${System.currentTimeMillis() - startTime}")
            startTime = System.currentTimeMillis()
            delay(110)
        }
    }

    coroutineScope {
        flow.sample(200).collect {
            println("sample value : $it")
        }
    }
    println()
}

/**
 * distinctUntilChangedBy : 去重操作符，判断连续的两个值是否重复，可以选择是否丢弃重复值。
 * keySelector: (T) -> Any? 指定用来判断是否需要比较的 key
 * 有点类似Recyclerview的DiffUtil机制。
 */
data class User(val name: String, val age: Int)

private fun `distinctUntilChangedBy`() = runBlocking {
    val flow = flowOf(User("Tome", 18), User("Tome", 19), User("Tome", 19))
    coroutineScope {
        flow.distinctUntilChangedBy { it.age }.collectLatest {
            println("value : ${it.toString()}")
        }
        println()
        flow.distinctUntilChanged { old, new ->
            old.name == new.name && old.age == new.age
        }.collectLatest {
            println("changed value : ${it.toString()}")
        }
    }
    println()
}

/**
 * distinctUntilChanged : 过滤用，distinctUntilChangedBy 的简化调用 。连续两个值一样，则跳过发送
 *
 */
private fun `distinctUntilChanged`() = runBlocking {
    val flow = flowOf(1, 1, 3, 1)

    coroutineScope {
        flow.distinctUntilChanged().collect {
            println("value : $it")
        }
    }
    println()
}