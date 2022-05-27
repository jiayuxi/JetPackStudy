package com.jiayx.jetpackstudy.paging3.data.mapper

/**
 *Created by yuxi_
on 2022/5/26
 */
interface Mapper<I, O> {
    fun map(input: I): O
}