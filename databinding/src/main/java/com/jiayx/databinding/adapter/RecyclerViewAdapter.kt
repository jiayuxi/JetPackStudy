package com.jiayx.databinding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.databinding.BR
import com.jiayx.databinding.R
import com.jiayx.databinding.bean.User
import com.jiayx.databinding.databinding.AdapterRecyclerViewBinding


/**
 *Created by yuxi_
on 2022/5/3
 */
class RecyclerViewAdapter(private val list: ArrayList<User>) :
    RecyclerView.Adapter<RecyclerViewAdapter.BindingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding: AdapterRecyclerViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_recycler_view, parent, false
        )
        val holder = BindingHolder(binding.root)
        holder.binding = binding
        return holder
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.binding?.setVariable(BR.user, list.getOrNull(position))
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class BindingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: AdapterRecyclerViewBinding? = null
    }
}