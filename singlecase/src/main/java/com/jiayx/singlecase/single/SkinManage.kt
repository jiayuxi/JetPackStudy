package com.jiayx.singlecase.single

import android.content.Context

/**
 *  author : Jia yu xi
 *  date : 2022/10/18 21:20:20
 *  description :
 */
class SkinManage private constructor(context: Context) {

    companion object : SingletonHolder<SkinManage, Context>(::SkinManage)

    fun method(string:String){
        println(string)
    }
}