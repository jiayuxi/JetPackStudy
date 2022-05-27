package com.jiayx.jetpackstudy.paging3.model

import kotlinx.serialization.Serializable

/**
 *Created by yuxi_
on 2022/5/26
 */
@Serializable
data class Hits(
    val id: Int = 0,
    val pageURL: String? = null,
    val type: String? = null,
    val tags: String? = null,
    val previewURL: String? = null,
    val previewWidth: Int = 0,
    val previewHeight: Int = 0,
    val webformatURL: String,
    val webformatWidth: Int = 0,
    val webformatHeight: Int = 0,
    val largeImageURL: String? = null,
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val imageSize: Long = 0,
    val views: Long = 0,
    val downloads: Long = 0,
    val collections: Int = 0,
    val likes: Int = 0,
    val comments: Int = 0,
    val user_id: Long = 0,
    val user: String? = null,
    val userImageURL: String? = null
)