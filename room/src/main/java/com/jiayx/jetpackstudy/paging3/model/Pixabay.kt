package com.jiayx.jetpackstudy.paging3.model

import kotlinx.serialization.Serializable

/**
 *Created by yuxi_
on 2022/5/26
 */
@Serializable
data class Pixabay(
    val total:Int,
    val totalHits:Int,
    val hits:List<Hits>)
