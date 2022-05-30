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
class Repository @Inject constructor(
    private val unsplashDatabase: UnsplashDatabase
) {
    companion object {
        private const val PAGE_SIZE = 10
        val config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = 5,
            initialLoadSize = 10,
            enablePlaceholders = false,
            maxSize = PAGE_SIZE * 3
        )
    }

    private var mArticleType: Int = 0
    private val pagingSourceFactory =
        { unsplashDatabase.articleDao().queryLocalArticle(mArticleType) }

    /**
     * 请求首页文章，
     * Room+network进行缓存
     */
    fun getHomeArticle(articleType: Int): Flow<PagingData<ArticleData>> {
        mArticleType = articleType
        return Pager(
            config = config,
            remoteMediator = ArticleRemoteMediator(RetrofitApi.create(), unsplashDatabase, 1),
            pagingSourceFactory = pagingSourceFactory
        ).flow.flowOn(Dispatchers.IO).catch { cause ->
            Log.e("jia_error", "getHomeArticle: $cause")
        }
    }
}