package com.jiayx.databinding.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

/**
 *Created by yuxi_
on 2022/5/2
 */
class DataViewModel : ViewModel() {
    val liveData: MutableLiveData<String> by lazy { MutableLiveData<String>().apply { "中华文明" } }
    fun setData() {
        liveData.value = "Code - " + Random.nextInt(100)
    }

    fun onClick(view: View){
        setData()
    }
}