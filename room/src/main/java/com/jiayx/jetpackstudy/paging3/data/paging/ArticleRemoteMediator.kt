package com.jiayx.jetpackstudy.paging3.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.data.remote.RetrofitApi
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.model.ArticleData
import com.jiayx.jetpackstudy.paging3.model.RemoteKey

/**
 *Created by yuxi_
on 2022/5/30
 */
private const val TAG = "jia_ArticleRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val retrofitApi: RetrofitApi,
    private val unsplashDatabase: UnsplashDatabase,
    private val articleType: Int
) : RemoteMediator<Int, ArticleData>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleData>
    ): MediatorResult {

        return try {
            Log.d(TAG, "load: $loadType")
            val pageKey: Int? = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    //使用remoteKey来获取下一个或上一个页面。
                    val remoteKey =
                        state.lastItemOrNull()?.id?.let {
                            unsplashDatabase.remoteKeyDao().remoteKeysArticleId(it, articleType)
                        }

                    //remoteKey' null '，这意味着在初始刷新后没有加载任何项目，也没有更多的项目要加载。
                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(true)
                    }
                    remoteKey.nextKey
                }
            }
            val page = pageKey ?: 0
            Log.d(TAG, "load: page $page")
            //从网络上请求数据
            val result = retrofitApi.getHomeArticle(page).data?.datas
            result?.forEach {
                it.articleType = articleType
            }
            val endOfPaginationReached = result?.isEmpty()

            unsplashDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //清空数据
                    unsplashDatabase.remoteKeyDao().clearRemoteKeys(articleType)
                    unsplashDatabase.articleDao().clearArticleByType(articleType)
                }
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (endOfPaginationReached!!) null else page + 1
                val keys = result.map {
                    RemoteKey(
                        articleId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        articleType = articleType
                    )
                }
                unsplashDatabase.remoteKeyDao().insertAll(keys)
                unsplashDatabase.articleDao().insertArticle(articleDataList = result)
            }
            return MediatorResult.Success(endOfPaginationReached!!)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}