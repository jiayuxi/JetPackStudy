package com.jiayx.jetpackstudy.paging3.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.data.remote.RetrofitApi
import com.jiayx.jetpackstudy.paging3.model.ArticleData
import com.jiayx.jetpackstudy.paging3.model.RemoteKey
import com.jiayx.jetpackstudy.ui.main.utils.AppHelper
import com.jiayx.jetpackstudy.ui.main.utils.isConnectedNetwork

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
    private val remoteKeyDao = unsplashDatabase.remoteKeyDao()
    private val articleDao = unsplashDatabase.articleDao()
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
                    /**
                     *
                     * 这里主要获取下一页数据的开始位置，可以理解为从什么地方开始加载下一页数据
                     * 这里有两种方式来获取下一页加载数据的位置
                     * 方式一：这种方式比较简单，当前页面最后一条数据是下一页的开始位置
                     * 方式二：比较麻烦，当前分页数据没有对应的远程 key，这个时候需要我们自己建表,
                     */

                    /**
                     * 方式一：这种方式比较简单，当前页面最后一条数据是下一页的开始位置
                     * 通过 load 方法的参数 state 获取当页面最后一条数据
                     */
                    val lastItem = state.lastItemOrNull()?:return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    Log.d(TAG, "load: lastItem  : $lastItem")
                    lastItem.page

                    /**
                     * 方式二：比较麻烦，当前分页数据没有对应的远程 key，这个时候需要我们自己建表
                     */
//                    val remoteKey = unsplashDatabase.withTransaction {
//                        remoteKeyDao.remoteKeysArticleType(articleType)
//                    }
//                    //remoteKey' null '，这意味着在初始刷新后没有加载任何项目，也没有更多的项目要加载。
//                    Log.d(TAG, "load: lastItem : remoteKey : ${remoteKey?.nextKey}")
//                    if (remoteKey?.nextKey == null) {
//                        return MediatorResult.Success(true)
//                    }
//                    remoteKey.nextKey
                }
            }
            if (!AppHelper.mContext.isConnectedNetwork()) {
                // 无网络加载本地数据
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            val page = pageKey ?: 0
            Log.d(TAG, "load: page $page")
            //从网络上请求数据
            val result = retrofitApi.getHomeArticle(page).data?.datas
            result?.forEach {
                it.articleType = articleType
                it.page = page +1
            }
            val endOfPaginationReached = result?.isEmpty()
            unsplashDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //清空数据
                    remoteKeyDao.clearRemoteKeys(articleType)
                    articleDao.clearArticleByType(articleType)
                }
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (endOfPaginationReached!!) null else page + 1
                Log.d(TAG, "load: nextKey : $nextKey")
                val keys = result.map {
                    RemoteKey(
                        articleId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        articleType = articleType
                    )
                }
                remoteKeyDao.insertAll(keys)
                articleDao.insertArticle(articleDataList = result)
            }
            return MediatorResult.Success(endOfPaginationReached!!)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}