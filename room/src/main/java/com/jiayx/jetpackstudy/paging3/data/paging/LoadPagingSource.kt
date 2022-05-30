package com.jiayx.jetpackstudy.paging3.data.paging

import android.text.TextUtils.isEmpty
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.utils.Constants
import com.jiayx.jetpackstudy.paging3.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.delay

/**
 *Created by yuxi_
on 2022/5/25
TODO 实现单一数据源以及如何从该数据源中查找数据，例如 Room，数据源的变动会直接映射到 UI 上
 */
class LoadPagingSource(private val unsplashApi: UnsplashApi) :
    PagingSource<Int, Hits>() {
    private val queryKey =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal").random()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hits> {

        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize
            Log.d("jia_paging3", "load: currentPage:$currentPage , pageSize:$pageSize ")
            var prevKey: Int? = null
            var nextKey: Int? = null
            val response = unsplashApi.loadImages(queryKey, currentPage, perPage = pageSize)
            val endOfPaginationReached = response.hits.isEmpty()
            if (currentPage == 1) {
                prevKey = null
                nextKey = ITEMS_PER_PAGE * 2 / ITEMS_PER_PAGE + 1
            } else {
                prevKey = currentPage - 1
                nextKey = if (endOfPaginationReached) null else currentPage + 1
            }
            Log.d("jia_paging3", "load: prevKey:$prevKey , nextKey:$nextKey ")
            if (response.hits.isNotEmpty()) {
                LoadResult.Page(
                    data = response.hits,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Page(data = emptyList(), null, null)
            }
        } catch (e: Exception) {
            Log.e("jia_error", "load: $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Hits>): Int? {
        return/* state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }*/ null
    }

}