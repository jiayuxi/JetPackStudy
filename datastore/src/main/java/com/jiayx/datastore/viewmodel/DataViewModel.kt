package com.jiayx.datastore.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiayx.datastore.model.UserModel
import com.jiayx.datastore.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/16
 */
@HiltViewModel
class DataViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    val userModel: Flow<UserModel> = repository.userModel

    fun saveData(userModel: UserModel) {
        viewModelScope.launch {
            repository.saveData(userModel)
        }
    }

}