package com.jiayx.hilt.di

import com.jiayx.hilt.init.SERVER_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 *Created by yuxi_
on 2022/5/17
 */
@InstallIn(ApplicationContext::class) // 跟 application 生命周期相关联
@Module
object NetworkModule {

    @Singleton // 单例只需一份
    @Provides // 如何获取实例
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        //拦截器
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}