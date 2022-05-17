package com.jiayx.datastore.di

import android.content.Context
import com.jiayx.datastore.repository.DataProtoRepository
import com.jiayx.datastore.manage.DataProtoStoreManage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *Created by yuxi_
on 2022/5/17
 */
@Module
@InstallIn(SingletonComponent::class)
object DataProtoStoreModule {

    @Provides
    @Singleton
    fun providesDataProtoStoreManage(@ApplicationContext context: Context) =
        DataProtoStoreManage(context)

    @Provides
    @Singleton
    fun providesDataProtoRepository(dataProtoStoreManage: DataProtoStoreManage): DataProtoRepository {
        return DataProtoRepository(dataProtoStoreManage)
    }
}