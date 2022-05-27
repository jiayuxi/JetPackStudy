package com.jiayx.jetpackstudy.paging3.data.paging

import android.text.TextUtils.isEmpty
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jiayx.jetpackstudy.paging3.data.remote.UnsplashApi
import com.jiayx.jetpackstudy.paging3.model.Hits
import com.jiayx.jetpackstudy.paging3.model.UnsplashImage
import com.jiayx.jetpackstudy.paging3.utils.Constants.ITEMS_PER_PAGE

/**
 *Created by yuxi_
on 2022/5/25
TODO 实现单一数据源以及如何从该数据源中查找数据，例如 Room，数据源的变动会直接映射到 UI 上
 */
class SearchPagingSource(private val unsplashApi: UnsplashApi, private val query: String) :
    PagingSource<Int, Hits>() {
    private val queryKey = arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal").random()
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hits> {
        val currentPage = params.key ?: 1
        return try {
            val response = unsplashApi.loadImages(queryKey,currentPage, perPage = ITEMS_PER_PAGE)
            val endOfPaginationReached = response.hits.isEmpty()
            if (response.hits.isEmpty()) {
                LoadResult.Page(
                    data = response.hits,
                    prevKey = if (currentPage == 1) null else currentPage - 1,//上一页
                    nextKey = if (endOfPaginationReached) null else currentPage + 1//下一页
                )
            } else {
                LoadResult.Page(data = emptyList(), null, null)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Hits>): Int? {
        return state.anchorPosition
    }

}