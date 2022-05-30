package com.jiayx.jetpackstudy.paging3.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiayx.jetpackstudy.paging3.utils.Constants

/**
 *Created by yuxi_
on 2022/5/30
 */
@Entity(tableName = Constants.UNSPLASH_IMAGE_TABLE)
data class ImageBean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val photoId: Int,
    val previewUrl: String?,
    val fullUrl: String?,
    val photoHeight: Int,
    val photoUser: String?,
    val photoLikes: Int,
    val photoFavorites: Int
)
