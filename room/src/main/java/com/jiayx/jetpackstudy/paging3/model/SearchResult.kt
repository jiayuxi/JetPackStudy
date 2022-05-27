package com.jiayx.jetpackstudy.paging3.model

import com.jiayx.jetpackstudy.paging3.model.UnsplashImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("results")
    val images: List<UnsplashImage>,
)