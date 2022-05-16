package com.jiayx.flow.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger

/**
 *Created by yuxi_
on 2022/5/16
协程上的并发操作
 */

fun main() {
    notsafeconcurrent()
    AtomicInt并发安全方式1()
    mutex并发安全方式2()
    semaphore并发安全方式3()
    testavoidaccessoutervariable()
}

/**
 * 协程上的并发
 * 并发安全
 */

private fun `notsafeconcurrent`() = runBlocking {
    var count = 0
    List(1000) {
        GlobalScope.launch {
            count++
        }
    }.joinAll()
    println("并发不全 result value: $count")
    println()
}

/**
 * 并发安全的 方式 1
 * 使用AtomicXXX
 */
private fun `AtomicInt并发安全方式1`() = runBlocking {
    var atomic = AtomicInteger(0)
    List(1000) {
        GlobalScope.launch {
            atomic.incrementAndGet()
        }
    }.joinAll()
    println("并发安全方式 1 value : ${atomic.get()}")
    println()
}

/**
 * 并发安全 方式 2
 * Mutex
 * Mutex：轻量级锁，它的lock和unlock从语义上与线程锁比较类似，
 * 之所以轻量是因为它在获取不到锁时不会阻塞线程，
 * 而是挂起等待锁的释放
 */

private fun `mutex并发安全方式2`() = runBlocking {
    var count = 0
    val mutex = Mutex()
    List(1000) {
        GlobalScope.launch {
            mutex.withLock {
                count++
            }
        }
    }.joinAll()
    println("并发安全的方式2：value: $count")
    println()
}

/**
 * 并发安全方式 3
 * Semaphore 信号量
 */
private fun `semaphore并发安全方式3`() = runBlocking {
    var count = 0
    val semaphore = Semaphore(1)
    List(1000) {
        GlobalScope.launch {
            semaphore.withPermit {
                count++
            }
        }
    }.joinAll()
    println("并发安全方式3 vale: $count")
    println()
    println()
}

/**
 * 避免访问外部可变状态
 */
private fun `testavoidaccessoutervariable`() = runBlocking {
    var count = 0
    val result = count + List(1000) {
        GlobalScope.async { 1 }
    }.sumOf {
        it.await()
    }
    println("避免访问外部可变状态：$result")
    println()
}