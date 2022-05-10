package com.jiayx.viewmodel.ui.main

import androidx.lifecycle.*
import com.jiayx.viewmodel.bean.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val mutableFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val stateFlow = mutableFlow.asStateFlow()
    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>()
    }
    fun getLiveDataValue()  = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        (1..10).forEach {
            emit(it)
        }
    }

    fun getUser(): MutableLiveData<List<User>> = users

    init {
        // 声明周期感知协程范围
        viewModelScope.launch {
            loadUsers()
        }
    }

    private fun loadUsers() {
        val list = arrayListOf<User>()
        (1..20).forEach {
            list.add(User("张三 $it", 20 + it))
        }
        users.value = list
    }
}