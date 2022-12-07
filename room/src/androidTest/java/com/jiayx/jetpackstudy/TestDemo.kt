package com.jiayx.jetpackstudy

import com.jiayx.jetpackstudy.ui.main.utils.transToString
import java.time.LocalDate

/**
 *Created by yuxi_
on 2022/3/14
 */

fun main() {
    println(transToString(System.currentTimeMillis()))
    val now = LocalDate.now()
    println("time : $now")
}