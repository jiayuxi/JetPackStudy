package com.jiayx.jetpackstudy.paging3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeysEntity(
    @PrimaryKey
    val remoteName: String,
    val nextKey: Int?
)