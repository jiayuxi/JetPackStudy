package com.jiayx.jetpackstudy.paging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.jiayx.jetpackstudy.paging3.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/29
 */
@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class PagingViewModel @Inject constructor(val repository: RepositoryImpl) : ViewModel() {
    // cachedIn 缓存数据
//    val getImages = repository.loadImage().cachedIn(viewModelScope)
    val getImages = repository.loadImage().cachedIn(viewModelScope).asLiveData()
    val getAllImages = repository.loadAllImage().cachedIn(viewModelScope)
}