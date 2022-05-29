package com.jiayx.jetpackstudy.paging3.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.binding.BindingViewHolder
import com.jiayx.jetpackstudy.databinding.Paging3ItemNetworkStateBinding
import com.jiayx.jetpackstudy.paging3.adapter.GalleryAdapter

/**
 *Created by yuxi_
on 2022/5/29
 */
class FooterAdapter(private val adapter: GalleryAdapter) :
    LoadStateAdapter<NetworkStateItemViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindData(loadState, 0)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.paging3_item_network_state, parent, false)
        return NetworkStateItemViewHolder(view) {
            adapter.retry() // 重新加载
        }
    }


}