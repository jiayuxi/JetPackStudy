package com.jiayx.datastore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiayx.datastore.UserProto
import com.jiayx.datastore.model.CustomModel
import com.jiayx.datastore.repository.DataProtoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by yuxi_
on 2022/5/17
 */
@HiltViewModel
class DataProtoViewModel @Inject constructor(private val dataRepository: DataProtoRepository) :
    ViewModel() {
    val userProto: Flow<UserProto> = dataRepository.userProto

    fun saveUserProto(name: String, email: String) {
        viewModelScope.launch {
            dataRepository.saveDataProto(name, email)
        }
    }

    // 自定义
    val userCustomData: Flow<CustomModel?> = dataRepository.userCustomData

    fun saveUserCustom(name: String, value: Int) {
        viewModelScope.launch {
            dataRepository.saveCustomData(CustomModel(name, value))
        }
    }
}