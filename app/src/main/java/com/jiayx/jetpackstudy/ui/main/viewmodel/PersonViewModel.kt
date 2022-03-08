package com.jiayx.jetpackstudy.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.jiayx.jetpackstudy.room.bean.Person
import com.jiayx.jetpackstudy.room.dao.PersonDao
import com.jiayx.jetpackstudy.room.database.PersonDatabase
import com.jiayx.jetpackstudy.room.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 *Created by yuxi_
on 2022/3/1
 */
class PersonViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val PAGE_SIZE = 30
        private const val ENABLE_PLACEHOLDER = true
    }

    private val repository: PersonRepository by lazy {
        PersonRepository(application)
    }

    val allPerson: Flow<PagingData<Person>> =
        Pager(config = PagingConfig(PAGE_SIZE, enablePlaceholders = ENABLE_PLACEHOLDER)) {
            repository.getAllPersons()
        }.flow
            .cachedIn(viewModelScope)
            .flowOn(Dispatchers.IO)
}