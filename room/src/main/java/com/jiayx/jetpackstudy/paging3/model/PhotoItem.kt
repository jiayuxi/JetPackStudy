package com.jiayx.jetpackstudy.paging3.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

/**
 *Created by yuxi_
on 2022/5/26
 */
@Serializable
data class PhotoItem(
    @SerializedName("webformatURL") val previewUrl: String?,
    @SerializedName("id") val photoId: Int,
    @SerializedName("largeImageURL") val fullUrl: String?,
    @SerializedName("webformatHeight") val photoHeight: Int,
    @SerializedName("user") val photoUser: String?,
    @SerializedName("likes") val photoLikes: Int,
    @SerializedName("favorites") val photoFavorites: Int
)
