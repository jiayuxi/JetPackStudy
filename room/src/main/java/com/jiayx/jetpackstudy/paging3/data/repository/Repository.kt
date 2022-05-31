package com.jiayx.jetpackstudy.paging3.data.repository

import android.util.Log
import androidx.paging.*
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.data.mapper.Entity2ItemModelMapper
import com.jiayx.jetpackstudy.paging3.data.paging.ArticleRemoteMediator
import com.jiayx.jetpackstudy.paging3.data.paging.PokemonRemoteMediator
import com.jiayx.jetpackstudy.paging3.data.remote.PokemonApi
import com.jiayx.jetpackstudy.paging3.data.remote.RetrofitApi
import com.jiayx.jetpackstudy.paging3.model.ArticleData
import com.jiayx.jetpackstudy.paging3.model.PokemonItemModel
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
            initialLoadSize = 30,
            enablePlaceholders = false,
            maxSize = PAGE_SIZE * 3
        )
        val pagingConfig = PagingConfig(
            // 每页显示的数据的大小
            pageSize = 30,

            // 开启占位符
            enablePlaceholders = true,

            // 预刷新的距离，距离最后一个 item 多远时加载数据
            // 默认为 pageSize
            prefetchDistance = 4,

            /**
             * 初始化加载数量，默认为 pageSize * 3
             *
             * internal const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
             * val initialLoadSize: Int = pageSize * DEFAULT_INITIAL_PAGE_MULTIPLIER
             */
            initialLoadSize = 30
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

    fun fetchPokemonList(): Flow<PagingData<PokemonItemModel>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = PokemonRemoteMediator(PokemonApi.create(), unsplashDatabase)
        ) {
            unsplashDatabase.pokemonDao().getPokemon()
        }.flow.map { pagingData ->
            pagingData.map { Entity2ItemModelMapper().map(it) }
        }.flowOn(Dispatchers.IO).catch { cause ->
            Log.e("jia_error", "getHomeArticle: $cause")
        }
    }


}