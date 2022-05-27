package com.jiayx.hilt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *Created by yuxi_
on 2022/5/25
 */
class HiltViewModel : ViewModel() {
   val mHiltLiveData = liveData<Int> {
       emit(888)
   }
}