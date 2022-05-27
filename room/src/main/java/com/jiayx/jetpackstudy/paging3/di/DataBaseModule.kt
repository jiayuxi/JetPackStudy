package com.jiayx.jetpackstudy.paging3.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.utils.Constants.UNSPLASH_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *Created by yuxi_
on 2022/5/24
 */
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): UnsplashDatabase {
        return Room.databaseBuilder(context, UnsplashDatabase::class.java, UNSPLASH_DATABASE)
            .build()
    }
}