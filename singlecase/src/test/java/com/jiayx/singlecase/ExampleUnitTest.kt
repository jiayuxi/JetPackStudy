package com.jiayx.singlecase

import com.jiayx.singlecase.single.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        fount01()
        fount02()
        fount03()
        fount04()
        fount05()
        fount06()
    }

    /**
     * 饿汉式单例模式
     */
    private fun fount01() {
        // Java
        println(SingletonObject01.getInstance().show())
        // kotlin
        println(SingletonObjectKt01.show())
    }

    /**
     * 懒汉式单例
     */
    private fun fount02() {
        //java
        println(SingletonObject02.getInstance().show())
        //kotlin
        println(SingletonObjectKt02.get()?.show())
    }

    /**
     * 线程安全的懒汉式单例
     */
    private fun fount03() {
        //java
        println(SingletonObject03.getInstance().show())
        //kotlin
        println(SingletonObjectKt03.get()?.show())
    }

    /**
     * 线程安全的双重检索懒汉式单例
     */
    private fun fount04() {
        //java
        println(SingletonObject04.getInstance().show())
        //kotlin
        println(SingletonObjectKt04.instance.show())
    }

    /**
     * 静态内部类单例
     */
    private fun fount05() {
        //java
        println(SingletonObject05.getInstance().show())
        //kotlin
        println(SingletonObjectKt05.instance.show())
    }

    /**
     * 枚举单例
     */
    private fun fount06() {
        //java
        println(SingletonObject06.INSTANCE.show())
        //kotlin
        println(SingletonObjectKt06.INSTANCE.show())
    }
}