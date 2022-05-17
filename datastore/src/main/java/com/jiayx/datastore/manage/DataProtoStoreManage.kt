package com.jiayx.datastore.manage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.jiayx.datastore.UserProto
import com.jiayx.datastore.proto.UserProtoSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

/**
 *Created by yuxi_
on 2022/5/17
 */
class DataProtoStoreManage(private val context: Context) {

    private val Context.userDataStore: DataStore<UserProto> by dataStore(
        fileName = "data.proto",
        serializer = UserProtoSerializer()
    )

    /**
     * saveDataProto
     */

    suspend fun saveDataProto(name: String, email: String) {
        context.userDataStore.updateData { userProto ->
            userProto.toBuilder().setName(name).setEmail(email).build()
        }
    }

    /**
     * 取值
     */

    val userProto: Flow<UserProto> = context.userDataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(UserProto.getDefaultInstance())
        } else {
            throw  exception
        }
    }
}