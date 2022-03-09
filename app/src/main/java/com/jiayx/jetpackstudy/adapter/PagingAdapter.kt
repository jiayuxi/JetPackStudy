package com.jiayx.jetpackstudy.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.jiayx.jetpackstudy.binding.BindingViewHolder
import com.jiayx.jetpackstudy.databinding.RoomFragmentItemBinding
import com.jiayx.jetpackstudy.room.bean.Person

/**
 *Created by yuxi_
on 2022/2/27
 */
class PagingAdapter(private val context: Context) :
    PagingDataAdapter<Person, BindingViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val person = getItem(position)
        person?.let {
            val binding = holder.binding as RoomFragmentItemBinding
            binding.itemName.text = it.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val binding =
            RoomFragmentItemBinding.inflate((LayoutInflater.from(parent.context)), parent, false)
        return BindingViewHolder(binding)
    }
}