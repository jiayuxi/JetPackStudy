package com.jiayx.datastore.repository

import com.jiayx.datastore.manage.DataProtoStoreManage
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/17
 */
class DataProtoRepository @Inject constructor(private val dataProtoStoreManage: DataProtoStoreManage) {

    suspend fun saveDataProto(name: String, email: String) {
        dataProtoStoreManage.saveDataProto(name, email)
    }

    val userProto = dataProtoStoreManage.userProto
}