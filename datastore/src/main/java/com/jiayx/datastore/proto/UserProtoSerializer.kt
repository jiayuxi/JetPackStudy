package com.jiayx.datastore.proto


import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.jiayx.datastore.UserProto
import java.io.InputStream
import java.io.OutputStream

/**
 *Created by yuxi_
on 2022/5/17
 */
class UserProtoSerializer : Serializer<UserProto> {

    override val defaultValue: UserProto = UserProto.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserProto {
        try {
            return UserProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            UserProto.getDefaultInstance()
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserProto, output: OutputStream) = t.writeTo(output)
}