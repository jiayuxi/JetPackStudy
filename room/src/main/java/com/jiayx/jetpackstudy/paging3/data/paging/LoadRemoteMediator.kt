package com.jiayx.jetpackstudy.paging3.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.data.mapper.ModelDatabaseMapper
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.model.ImageBean
import com.jiayx.jetpackstudy.paging3.model.UnsplashRemoteKeys
import com.jiayx.jetpackstudy.paging3.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.delay
import retrofit2.HttpException

/**
 *Created by yuxi_
on 2022/5/25
TODO 实现加载网络分页数据并更新到数据库中，但是数据源的变动不能直接映射到 UI 上
 */
@OptIn(ExperimentalPagingApi::class)
class LoadRemoteMediator(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
) : RemoteMediator<Int, ImageBean>() {
    private val queryKey =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal").random()
    private val unsplashImageDao = unsplashDatabase.unsplashImageDao()
    private val unsplashRemoteKeysDao = unsplashDatabase.unsplashRemoteKeysDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageBean>
    ): MediatorResult {
        delay(2000)
        return try {
            val pageKey = when (loadType) {
                LoadType.REFRESH -> {
                    null
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = unsplashDatabase.withTransaction {
                        unsplashRemoteKeysDao.getRemoteKeys(remotePokemon)
                    }
                    if (remoteKey?.nextPage == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextPage
                }
            }
            // 第二步： 请问网络分页数据
            val currentPage = pageKey ?: 1
            Log.d(
                "jia_paging3",
                "load: currentPage : $currentPage ,state.config.pageSize : ${state.config.pageSize} , initialLoadSize:${state.config.initialLoadSize}"
            )
            val response = unsplashApi.loadImages(
                q = "cat",
                page = currentPage,
                perPage = state.config.pageSize
            ).hits
            val endOfPaginationReached = response.isEmpty()
            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1
            unsplashDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    unsplashImageDao.deleteAllImages()
                    unsplashRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.map {
                    UnsplashRemoteKeys(
                        id = remotePokemon,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                val imageBean = response.map {
                    ModelDatabaseMapper().map(it)
                }
                unsplashRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                unsplashImageDao.addImages(imageBean)
            }
            //TODO 返回加载完成
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.e("jia_error", "load: $e")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e("jia_error", "load: $e")
            MediatorResult.Error(e)
        }
    }

    companion object {
        private val remotePokemon = 100
    }
}