package com.jiayx.datastore.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.jiayx.datastore.model.CustomModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 *Created by yuxi_
on 2022/5/17
 */
// 自定义
class CustomModelSerializer : Serializer<CustomModel?> {
    override val defaultValue: CustomModel? get() = null

    override suspend fun readFrom(input: InputStream): CustomModel? {
        try {
            val str = input.readBytes().decodeToString()
            if (str.isNullOrBlank()) return null
            return Json.decodeFromString(CustomModel.serializer(), str)
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPres", serialization)
        }
    }

    override suspend fun writeTo(t: CustomModel?, output: OutputStream) {
        if (t == null) return
        output.write(Json.encodeToString(CustomModel.serializer(), t).encodeToByteArray())
    }
}