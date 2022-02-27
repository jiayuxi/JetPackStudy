package com.jiayx.jetpackstudy.room.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 *Created by yuxi_
on 2022/2/27
 */
@Entity(tableName = "student")
class StudentBean {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var longId: Int = 0

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String? = null

    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    var age: Int = 0

    constructor(longId: Int, name: String?, age: Int) {
        this.longId = longId
        this.name = name
        this.age = age
    }

    @Ignore
    constructor(name: String?, age: Int) {
        this.name = name
        this.age = age
    }

    @Ignore
    constructor(longId: Int) {
        this.longId = longId
    }

    override fun toString(): String {
        return "Student(longId=$longId, name=$name, age=$age)"
    }


}