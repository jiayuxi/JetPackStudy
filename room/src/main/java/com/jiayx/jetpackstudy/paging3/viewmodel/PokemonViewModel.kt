package com.jiayx.jetpackstudy.paging3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jiayx.jetpackstudy.paging3.data.repository.Repository
import com.jiayx.jetpackstudy.paging3.model.PokemonItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/31
 */
@HiltViewModel
class PokemonViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    // 通过 paging3 加载数据
    fun postOfData(): LiveData<PagingData<PokemonItemModel>> =
        repository.fetchPokemonList().cachedIn(viewModelScope).asLiveData()
}