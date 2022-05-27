package com.jiayx.jetpackstudy.paging3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jiayx.jetpackstudy.paging3.data.local.dao.UnsplashImageDao
import com.jiayx.jetpackstudy.paging3.data.local.dao.UnsplashRemoteKeysDao
import com.jiayx.jetpackstudy.paging3.model.UnsplashRemoteKeys
import com.jiayx.jetpackstudy.paging3.model.UnsplashImage

/**
 *Created by yuxi_
on 2022/5/24
 */
@Database(
    entities = [UnsplashImage::class, UnsplashRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class UnsplashDatabase : RoomDatabase() {
    abstract fun unsplashImageDao(): UnsplashImageDao
    abstract fun unsplashRemoteKeysDao(): UnsplashRemoteKeysDao
}