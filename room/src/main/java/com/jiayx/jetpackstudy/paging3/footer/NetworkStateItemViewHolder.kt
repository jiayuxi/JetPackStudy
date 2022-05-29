package com.jiayx.jetpackstudy.paging3.footer

import android.view.View
import androidx.paging.LoadState
import com.jiayx.jetpackstudy.binding.DataBindingViewHolder
import com.jiayx.jetpackstudy.databinding.Paging3ItemNetworkStateBinding

/**
 *Created by yuxi_
on 2022/5/29
 */
class NetworkStateItemViewHolder(view: View, private val retryCallback: () -> Unit) :
    DataBindingViewHolder<LoadState>(view) {
    private val mBinding: Paging3ItemNetworkStateBinding by viewHolderBinding(view)
    override fun bindData(data: LoadState, position: Int) {
        mBinding.apply {
            // 正在加载，显示进度条
            progressBar.isVisible = data is LoadState.Loading
            //加载失败，显示并点击重试按钮
            retryButton.isVisible = data is LoadState.Error
            retryButton.setOnClickListener { retryCallback() }
            // 加载失败显示错误原因
            errorMsg.isVisible = !(data as? LoadState.Error)?.error?.message.isNullOrBlank()
            errorMsg.text = (data as? LoadState.Error)?.error?.message

            executePendingBindings()
        }
    }
}

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }