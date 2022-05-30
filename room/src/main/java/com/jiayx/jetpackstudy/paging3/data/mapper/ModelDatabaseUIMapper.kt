package com.jiayx.jetpackstudy.paging3.data.mapper

import com.google.gson.annotations.SerializedName
import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.model.ImageBean
import com.jiayx.jetpackstudy.paging3.model.PhotoItem
import dagger.Module

/**
 *Created by yuxi_
on 2022/5/26
 */
class ModelDatabaseUIMapper : Mapper<ImageBean, PhotoItem> {
    override fun map(input: ImageBean): PhotoItem = PhotoItem(
        previewUrl = input.previewUrl,
        photoId = input.photoId,
        fullUrl = input.fullUrl,
        photoHeight = input.photoHeight,
        photoUser = input.photoUser,
        photoLikes = input.photoLikes,
        photoFavorites = 0
    )
}