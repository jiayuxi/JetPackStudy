package com.jiayx.jetpackstudy.paging3.di

import android.util.Log
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *Created by yuxi_
on 2022/5/24
 */
@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("jia_okhttp", it) })
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder().addInterceptor(logger).readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS).build()

    }

    @Provides
    @Singleton
    fun provideRetrofit(okhttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi {
        return retrofit.create(UnsplashApi::class.java)
    }
}