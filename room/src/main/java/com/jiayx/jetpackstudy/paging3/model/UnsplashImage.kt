package com.jiayx.jetpackstudy.paging3.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiayx.jetpackstudy.paging3.utils.Constants.UNSPLASH_IMAGE_TABLE
import kotlinx.serialization.Serializable

/**
 *Created by yuxi_
on 2022/5/24
 */
@Serializable
@Entity(tableName = UNSPLASH_IMAGE_TABLE)
data class UnsplashImage(@PrimaryKey(autoGenerate = false)
                          val id: String,
                         @Embedded
                          val urls: Urls,
                         val likes: Int,
                         @Embedded
                          val user: User
)
