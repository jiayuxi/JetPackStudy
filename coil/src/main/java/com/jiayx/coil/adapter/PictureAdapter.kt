package com.jiayx.coil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.coil.BR
import com.jiayx.coil.R
import com.jiayx.coil.bean.Pictures
import com.jiayx.coil.databinding.ItemPictureLayoutBinding

/**
 *Created by yuxi_
on 2022/5/3
 */

class PictureAdapter:
    RecyclerView.Adapter<PictureAdapter.BindingHolder>() {
    private var arrayList: ArrayList<Pictures>? = null

    fun setData(list: ArrayList<Pictures>) {
        this.arrayList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding: ItemPictureLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_picture_layout, parent, false
        )
        val holder = BindingHolder(binding.root)
        holder.bindingHolder = binding
        return holder
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.bindingHolder?.setVariable(BR.picture, arrayList?.getOrNull(position))
        holder.bindingHolder?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }

    // 绑定
    inner class BindingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bindingHolder: ItemPictureLayoutBinding? = null
    }
}