package com.jiayx.jetpackstudy.ui.main.utils

import android.content.Context

/**
 *Created by yuxi_
on 2022/5/31
 */
object AppHelper {
    lateinit var mContext: Context
    fun init(context: Context) {
        this.mContext = context
    }
}