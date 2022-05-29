package com.jiayx.jetpackstudy.binding

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *Created by yuxi_
on 2022/5/29
 */
abstract class DataBindingViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {
    @Throws(Exception::class)
    abstract fun bindData(data: T, position: Int)

    fun view() = view

    fun context(): Context {
        return view.context
    }

    inline fun <reified T : ViewDataBinding> viewHolderBinding(view: View): Lazy<T> =
        lazy {
            requireNotNull(DataBindingUtil.bind<T>(view)) { "cannot find the matched layout." }
        }
}