package com.jiayx.jetpackstudy.paging3.data.mapper

import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.model.ImageBean

/**
 *Created by yuxi_
on 2022/5/30
 */
class ModelDatabaseMapper : Mapper<Hits, ImageBean> {
    override fun map(input: Hits): ImageBean {
        return ImageBean(
            0,
            previewUrl = input.webformatURL,
            photoId = input.id,
            fullUrl = input.largeImageURL,
            photoHeight = input.webformatHeight,
            photoUser = input.user,
            photoLikes = input.likes,
            photoFavorites = 0
        )
    }
}