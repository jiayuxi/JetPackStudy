package com.jiayx.livedata.event

import androidx.lifecycle.MutableLiveData
import kotlin.contracts.ReturnsNotNull

/**
 *Created by yuxi_
on 2022/5/1
 */
class LiveDataBusData {
    // 饿汉式双重检索
    companion object {
        val instance: LiveDataBusData by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LiveDataBusData()
        }
    }

    private val bus by lazy { mutableMapOf<String, MutableLiveData<Any>>() }

    fun <T> with(key: String, type: Class<T>): MutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus += key to MutableLiveData<Any>()
        }
        return bus[key] as MutableLiveData<T>
    }

    fun with(target: String): MutableLiveData<Any> {
        return with(target, Any::class.java)
    }

    fun remove(key:String){
        if(bus.containsKey(key)){
            bus.remove(key)
        }
    }
}
