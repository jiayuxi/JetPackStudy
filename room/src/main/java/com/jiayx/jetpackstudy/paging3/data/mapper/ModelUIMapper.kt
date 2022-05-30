package com.jiayx.jetpackstudy.paging3.data.mapper

import com.google.gson.annotations.SerializedName
import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.model.PhotoItem
import dagger.Module

/**
 *Created by yuxi_
on 2022/5/26
 */
class ModelUIMapper : Mapper<Hits, PhotoItem> {
    override fun map(input: Hits): PhotoItem = PhotoItem(
        previewUrl = input.webformatURL,
        photoId = input.id,
        fullUrl = input.largeImageURL,
        photoHeight = input.webformatHeight,
        photoUser = input.user,
        photoLikes = input.likes,
        photoFavorites = 0
    )
}