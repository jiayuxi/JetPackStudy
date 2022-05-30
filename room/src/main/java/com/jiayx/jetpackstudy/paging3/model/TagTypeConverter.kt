package com.jiayx.jetpackstudy.paging3.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *Created by yuxi_
on 2022/5/30
 */
class TagTypeConverter {
    @TypeConverter
    fun stringToObject(value: String): List<Tag> {
        val listType = object : TypeToken<List<Tag>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Tag>): String {
        return Gson().toJson(list)
    }
}