package com.jiayx.jetpackstudy.paging3.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.jiayx.jetpackstudy.paging3.data.mapper.ModelUIMapper
import com.jiayx.jetpackstudy.paging3.data.paging.LoadPagingSource
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.model.PhotoItem
import com.jiayx.jetpackstudy.paging3.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/29
 */
class Repository @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun loadImage(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE,// 每页的大小
                prefetchDistance = 1,// 用于表示距离底部多少条数据开始预加载
                initialLoadSize = Constants.ITEMS_PER_PAGE * 2//初始化加载的大小，一般设置大于pageSize
            ),
            pagingSourceFactory = {
                LoadPagingSource(unsplashApi)
            }).flow.map { hilts ->
            hilts.map { ModelUIMapper().map(it) }
        }
    }
}