package com.jiayx.jetpackstudy.paging3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.jiayx.jetpackstudy.BR
import com.jiayx.jetpackstudy.binding.BindingViewHolder
import com.jiayx.jetpackstudy.databinding.GalleryCellBinding
import com.jiayx.jetpackstudy.paging3.model.PhotoItem

/**
 *Created by yuxi_
on 2022/5/29
 */
class GalleryAdapter : PagingDataAdapter<PhotoItem, BindingViewHolder>(ItemCallback) {


    object ItemCallback : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

        override fun getChangePayload(oldItem: PhotoItem, newItem: PhotoItem): Any? {
            return if (oldItem.photoId != newItem.photoId) newItem.photoId else null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val dataBinding =
            GalleryCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            val binding: GalleryCellBinding = holder.binding as GalleryCellBinding
            // 第一种方法
            // binding.setVariable(BR.photoItem,it)
            // 第二种方法
            binding.photoItem = it
        }
    }
}