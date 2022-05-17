package com.jiayx.datastore.repository

import com.jiayx.datastore.manage.DataStoreManage
import com.jiayx.datastore.model.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/16
 */
class DataRepository @Inject constructor(private val dataStoreManage: DataStoreManage) {
    /**
     * 保存内容
     */
    suspend fun saveData(userModel: UserModel) {
        dataStoreManage.saveData(userModel)
    }

    val userModel : Flow<UserModel> = dataStoreManage.userModel
}