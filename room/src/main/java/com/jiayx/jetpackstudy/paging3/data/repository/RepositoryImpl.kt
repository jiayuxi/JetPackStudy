package com.jiayx.jetpackstudy.paging3.data.repository

import android.util.Log
import androidx.paging.*
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.data.mapper.Mapper
import com.jiayx.jetpackstudy.paging3.data.mapper.ModelDatabaseUIMapper
import com.jiayx.jetpackstudy.paging3.data.mapper.ModelUIMapper
import com.jiayx.jetpackstudy.paging3.data.paging.ArticleRemoteMediator
import com.jiayx.jetpackstudy.paging3.data.paging.LoadPagingSource
import com.jiayx.jetpackstudy.paging3.data.paging.LoadRemoteMediator
import com.jiayx.jetpackstudy.paging3.data.remote.RetrofitApi
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.model.ArticleData
import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.model.PhotoItem
import com.jiayx.jetpackstudy.paging3.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/29
 */
@OptIn(ExperimentalPagingApi::class)
class RepositoryImpl @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase,
) {
    fun loadImage(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE,// 每页的大小
                enablePlaceholders = false,
                prefetchDistance = 1,// 用于表示距离底部多少条数据开始预加载
                initialLoadSize = Constants.ITEMS_PER_PAGE * 2//初始化加载的大小，一般设置大于pageSize
            ),
            pagingSourceFactory = {
                LoadPagingSource(unsplashApi)
            }).flow.map { hilts ->
            hilts.map { ModelUIMapper().map(it) }
        }.flowOn(Dispatchers.IO)
    }

    fun loadAllImage(): Flow<PagingData<PhotoItem>> {

        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE,// 每页的大小
                prefetchDistance = 5,
                initialLoadSize = 10,//初始化加载的大小，一般设置大于pageSize
                enablePlaceholders = false,
//                maxSize = Constants.ITEMS_PER_PAGE * 3
            ),
            remoteMediator = LoadRemoteMediator(unsplashApi, unsplashDatabase),
        ) {
            unsplashDatabase.unsplashImageDao().getAllImages()
        }.flow.map { hits ->
            hits.map { ModelDatabaseUIMapper().map(it) }
        }.catch { cause ->
            Log.e("jia_error", "Caught loadAllImage: $cause")
        }.flowOn(Dispatchers.IO)

    }
}