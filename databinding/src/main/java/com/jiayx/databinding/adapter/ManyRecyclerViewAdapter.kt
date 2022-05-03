package com.jiayx.databinding.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.databinding.BR
import com.jiayx.databinding.R
import com.jiayx.databinding.bean.User
import com.jiayx.databinding.databinding.AdapterRecyclerViewBinding


/**
 *Created by yuxi_
on 2022/5/3
 */
class ManyRecyclerViewAdapter(private val list: ArrayList<User>) :
    RecyclerView.Adapter<ManyRecyclerViewAdapter.BindingHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position < 5) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        var binding: ViewDataBinding? = null
        binding = if (viewType == 0) {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.adapter_recycler_view, parent, false
            )
        } else {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_recycler_view_other,
                parent,
                false
            )
        }

        val holder = BindingHolder(binding.root)
        holder.binding = binding
        return holder
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        Log.d("adapter_binding", "onBindViewHolder: ${holder.itemViewType}")
        holder.binding?.setVariable(BR.user, list.getOrNull(position))
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class BindingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ViewDataBinding? = null
    }
}