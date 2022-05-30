package com.jiayx.jetpackstudy.paging3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jiayx.jetpackstudy.paging3.data.local.dao.ArticleDao
import com.jiayx.jetpackstudy.paging3.data.local.dao.RemoteKeyDao
import com.jiayx.jetpackstudy.paging3.data.local.dao.UnsplashImageDao
import com.jiayx.jetpackstudy.paging3.data.local.dao.UnsplashRemoteKeysDao
import com.jiayx.jetpackstudy.paging3.model.*

/**
 *Created by yuxi_
on 2022/5/24
 */
@Database(
    entities = [ImageBean::class, UnsplashRemoteKeys::class,ArticleData::class,RemoteKey::class],
    version = 5,
    exportSchema = false
)
abstract class UnsplashDatabase : RoomDatabase() {
    abstract fun unsplashImageDao(): UnsplashImageDao
    abstract fun unsplashRemoteKeysDao(): UnsplashRemoteKeysDao
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao

}