package com.jiayx.datastore.di

import android.content.Context
import com.jiayx.datastore.repository.DataRepository
import com.jiayx.datastore.manage.DataStoreManage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *Created by yuxi_
on 2022/5/16
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providerDataStoreModule(@ApplicationContext context:Context) = DataStoreManage(context)

    @Provides
    @Singleton
    fun providerDataRepository(dataStoreManage: DataStoreManage) : DataRepository {
        return DataRepository(dataStoreManage)
    }
}