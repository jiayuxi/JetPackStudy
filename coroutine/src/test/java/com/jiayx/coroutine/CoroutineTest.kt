package com.jiayx.coroutine

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoroutineTest {
    @Test
    fun `test CoroutineContext`() = runBlocking {
        launch (Dispatchers.Default + CoroutineName("test")){
            println("I'm working in thread :${Thread.currentThread().name}")
        }
    }
}