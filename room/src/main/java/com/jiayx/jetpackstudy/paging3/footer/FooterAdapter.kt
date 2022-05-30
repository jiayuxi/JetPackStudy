package com.jiayx.jetpackstudy.paging3.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.binding.BindingViewHolder
import com.jiayx.jetpackstudy.databinding.Paging3ItemNetworkStateBinding
import com.jiayx.jetpackstudy.paging3.adapter.GalleryAdapter

/**
 *Created by yuxi_
on 2022/5/29
 */
class FooterAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<NetworkStateItemViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        //水平居中
        val params = holder.itemView.layoutParams
        if (params is StaggeredGridLayoutManager.LayoutParams) {
            params.isFullSpan = true
        }
        holder.bindData(loadState, 0)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paging3_item_network_state, parent, false)
        return NetworkStateItemViewHolder(view) {
            retry() // 重新加载
        }
    }


}