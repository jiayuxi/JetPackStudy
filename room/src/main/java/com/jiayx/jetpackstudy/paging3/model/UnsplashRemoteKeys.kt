package com.jiayx.jetpackstudy.paging3.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiayx.jetpackstudy.paging3.utils.Constants.UNSPLASH_REMOTE_KEYS_TABLE

@Entity(tableName = UNSPLASH_REMOTE_KEYS_TABLE)
data class UnsplashRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val prevPage: Int?,
    val nextPage: Int?
)