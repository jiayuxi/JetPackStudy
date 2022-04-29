package com.jiayx.flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/1
 */
class NumberViewModel : ViewModel() {
    val number = MutableStateFlow(0)
    var value = 0
    fun increment() {
        number.value++
    }

    fun decrement() {
        number.value--
    }
}