package com.jiayx.jetpackstudy.room.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 *Created by yuxi_
on 2022/3/1
 */
@Entity(tableName = "person")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    var isSelect: Boolean = false
)
