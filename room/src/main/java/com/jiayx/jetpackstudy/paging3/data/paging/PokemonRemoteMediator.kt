package com.jiayx.jetpackstudy.paging3.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jiayx.jetpackstudy.paging3.data.local.UnsplashDatabase
import com.jiayx.jetpackstudy.paging3.data.remote.PokemonApi
import com.jiayx.jetpackstudy.paging3.model.PokemonEntity
import com.jiayx.jetpackstudy.paging3.model.RemoteKeysEntity
import com.jiayx.jetpackstudy.ui.main.utils.AppHelper
import com.jiayx.jetpackstudy.ui.main.utils.isConnectedNetwork
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val pokemonApi: PokemonApi,
    private val db: UnsplashDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    private val pokemonDao = db.pokemonDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        try {

            /**
             * 在这个方法内将会做三件事
             *
             * 1. 参数 LoadType 有个三个值，关于这三个值如何进行判断
             *      LoadType.REFRESH
             *      LoadType.PREPEND
             *      LoadType.APPEND
             *
             * 2. 访问网络数据
             *
             * 3. 将网路插入到本地数据库中
             */

            Log.d(TAG, "load: loadType : $loadType")
            // 第一步： 判断 LoadType
            val pageKey = when (loadType) {
                // 首次访问 或者调用 PagingDataAdapter.refresh()
                //在初始化刷新的使用
                LoadType.REFRESH -> null

                // 在当前加载的数据集的开头加载数据时
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> { // 下来加载更多时触发

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
//                    val lastItem = state.lastItemOrNull()
//                    Log.d(TAG, "load: lastItem: ${lastItem.toString()}")
//                    if (lastItem == null) {
//                        return MediatorResult.Success(
//                            endOfPaginationReached = true
//                        )
//                    }
//                    lastItem.page

                    /**
                     * 方式二：比较麻烦，当前分页数据没有对应的远程 key，这个时候需要我们自己建表
                     */
                    val remoteKey = db.withTransaction {
                        db.remoteKeysDao().getRemoteKeys(remotePokemon)
                    }
                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextKey
                }
            }

            if (!AppHelper.mContext.isConnectedNetwork()) {
                // 无网络加载本地数据
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            // 第二步： 请问网络分页数据
            val page = pageKey ?: 0
            val result = pokemonApi.fetchPokemonList(
                state.config.pageSize,
                page * state.config.pageSize
            ).results

            Log.d(TAG, "load: page : $page")

            val endOfPaginationReached = result.isEmpty()

            val item = result.map {
                PokemonEntity(
                    name = it.name,
                    url = it.getImageUrl(),
                    remoteName = remotePokemon,
                    page = page + 1
                )
            }

            // 第三步： 插入数据库
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys(remotePokemon)
                    pokemonDao.clearPokemon(remotePokemon)
                }
                val nextKey = if (endOfPaginationReached) null else page + 1
                val entity = RemoteKeysEntity(
                    remoteName = remotePokemon,
                    nextKey = nextKey
                )
                remoteKeysDao.insertAll(entity)
                pokemonDao.insertPokemon(item)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            Log.e("jia_error", "load: $e")
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e("jia_error", "load: $e")
            return MediatorResult.Error(e)
        }
    }

    companion object {
        private val TAG = "jia_PokemonRemoteMediator"
        private val remotePokemon = "pokemon"
    }


}