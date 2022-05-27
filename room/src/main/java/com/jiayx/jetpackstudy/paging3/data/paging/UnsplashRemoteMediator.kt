package com.jiayx.jetpackstudy.paging3.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.model.UnsplashImage
import com.jiayx.jetpackstudy.paging3.utils.Constants.ITEMS_PER_PAGE

/**
 *Created by yuxi_
on 2022/5/25
TODO 实现加载网络分页数据并更新到数据库中，但是数据源的变动不能直接映射到 UI 上
 */
@ExperimentalPagingApi
class UnsplashRemoteMediator(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
) : RemoteMediator<Int, UnsplashImage>() {

    private val unsplashImageDao = unsplashDatabase.unsplashImageDao()
    private val unsplashRemoteKeysDao = unsplashDatabase.unsplashRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImage>
    ): MediatorResult {
        return try {
//              val currentPage = when(loadType){
//                LoadType.REFRESH -> {
//
//                }
//                LoadType.PREPEND -> {
//
//                }
//                LoadType.APPEND -> {
//
//                }
//              }
//            val response = unsplashApi.getAllImages(page =  currentPage,perPage = ITEMS_PER_PAGE)

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    suspend fun getRemoteKeyClosestToCurrentPosition(){

    }
}