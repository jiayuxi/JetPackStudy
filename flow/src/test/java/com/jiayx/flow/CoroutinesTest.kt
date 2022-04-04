package com.jiayx.flow

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoroutinesTest {
    @Test
    fun `test coroutine builder`() = runBlocking {
        launch {
            delay(200)
            println("job1 finished")
        }
        val job2 = async {
            delay(200)
            println("job2 finished")
            "result job2"
        }
        println(job2.await())
    }

    @Test
    fun `test coroutine join`() = runBlocking {
        val job1 = launch {
            delay(2000)
            println("one")
        }
        job1.join()
        val job2 = launch {
            delay(200)
            println("two")
        }
        val job3 = launch {
            delay(200)
            println("three")
        }
    }

    @Test
    fun `test coroutine await`() = runBlocking {
        val job1 = async {
            delay(2000)
            println("one")
        }
        job1.await()
        val job2 = async {
            delay(200)
            println("two")
        }
        val job3 = async {
            delay(200)
            println("three")
        }
    }
    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
    @Test
    fun `test async`() = runBlocking {
        val one = async {
           log("计算one的值")
            doOne()
        }
        val two = async {
            log("计算two的值")
            doTwo()
        }
        println("结构性编发：result: ${one.await()} + ${two.await()}")
    }

    suspend fun doOne(): Int {
        delay(1000)
        return 14
    }

    suspend fun doTwo(): Int {
        delay(1000)
        return 13
    }
}