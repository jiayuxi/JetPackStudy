package com.jiayx.jetpackstudy.paging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jiayx.jetpackstudy.paging3.data.repository.Repository
import com.jiayx.jetpackstudy.paging3.model.ArticleData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/31
 */
@HiltViewModel
class ArticleViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    /**
     * 请求首页文章数据
     */
    fun articlePagingFlow(): Flow<PagingData<ArticleData>> =
        repository.getHomeArticle(1).cachedIn(viewModelScope)
}