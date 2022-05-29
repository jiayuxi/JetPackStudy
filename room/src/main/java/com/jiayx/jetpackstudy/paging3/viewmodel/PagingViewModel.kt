package com.jiayx.jetpackstudy.paging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jiayx.jetpackstudy.paging3.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/29
 */
@HiltViewModel
class PagingViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    // cachedIn 缓存数据
    val getImages = repository.loadImage().cachedIn(viewModelScope)
}