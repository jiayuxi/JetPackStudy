package com.jiayx.datastore.manage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.jiayx.datastore.UserProto
import com.jiayx.datastore.model.CustomModel
import com.jiayx.datastore.proto.CustomModelSerializer
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

    // 自定义存储
    private val Context.customDataStore: DataStore<CustomModel?> by dataStore(
        fileName = ("data.json"),
        serializer = CustomModelSerializer()
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
     * bean 文件形式保存
     */
    suspend fun saveDataBean(userProto: UserProto) {
        context.userDataStore.updateData { userProto }
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

    /**
     * 自定义 saveDataProto
     */

    suspend fun saveCustomData(customModel: CustomModel) {
        context.customDataStore.updateData {
            customModel
        }
    }

    /**
     * 取值
     */

    val userCustomData: Flow<CustomModel?> = context.customDataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(CustomModel("", 0))
        } else {
            throw  exception
        }
    }

    /**
     * logout
     */
    suspend fun logout() {
        context.customDataStore.updateData { null }
    }
}