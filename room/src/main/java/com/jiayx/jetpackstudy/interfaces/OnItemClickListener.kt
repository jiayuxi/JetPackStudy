package com.jiayx.jetpackstudy.interfaces

import android.view.View

/**
 *Created by yuxi_
on 2022/5/20
 */
interface OnItemClickListener {
    /**
     * 当ItemView的单击事件触发时调用
     */
    fun onItemClick(view: View?, position: Int)

    /**
     * 当ItemView的长按事件触发时调用
     */
    fun onItemLongClick(view: View?, position: Int)

    /**
     * 当ItemView的双击事件触发时调用
     */
    fun onItemDoubleClick(view: View?, position: Int)
}