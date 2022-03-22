package com.jiayx.jetpackstudy.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.FeatureGroupInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.jiayx.jetpackstudy.binding.BindingViewHolder
import com.jiayx.jetpackstudy.databinding.PagingActivityBinding
import com.jiayx.jetpackstudy.databinding.PagingFragmentItemBinding
import com.jiayx.jetpackstudy.databinding.RoomFragmentItemBinding
import com.jiayx.jetpackstudy.room.bean.Person

/**
 *Created by yuxi_
on 2022/2/27
 */
class PagingAdapter(private val context: Context) :
    PagingDataAdapter<Person, BindingViewHolder>(diffCallback) {
    var isDelete = false
    open var setOnClickListener: ((position: Int) -> Unit)? = null

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.name == newItem.name && oldItem.isSelect == newItem.isSelect
            }

            override fun getChangePayload(oldItem: Person, newItem: Person): Any? {
                return if (oldItem.isSelect != newItem.isSelect) newItem.isSelect else null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val binding =
            PagingFragmentItemBinding.inflate((LayoutInflater.from(parent.context)), parent, false)
        return BindingViewHolder(binding)
    }

//    override fun onBindViewHolder(
//        holder: BindingViewHolder,
//        position: Int,
//        payloads: MutableList<Any>
//    ) {
//        val person = getItem(position)
//        person?.let { person ->
//            val binding = holder.binding as PagingFragmentItemBinding
//            if (payloads?.isNotEmpty()) {
//                binding.checkbox.isChecked = person.isSelect
//            } else {
//                binding.itemName.text = person.name
//                binding.checkbox.visibility = if (isDelete) View.VISIBLE else View.GONE
//            }
//            binding.checkbox.isChecked = person.isSelect
//            binding.root.setOnClickListener {
//                if (!isDelete) return@setOnClickListener
//                Log.d("adapter_log", "onBindViewHolder: position : $position")
//                setOnClickListener?.invoke(position)
//            }
//        }
//    }

    fun getData(position: Int): Person? {
        return getItem(position)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        Log.d("adapter_log", "onBindViewHolder: 2")
        val person = getItem(position)
        person?.let { person ->
            val binding = holder.binding as PagingFragmentItemBinding
            binding.itemName.text = person.name
            binding.checkbox.visibility = if (isDelete) View.VISIBLE else View.GONE
            binding.checkbox.isChecked = person.isSelect
            binding.root.setOnClickListener {
                if (!isDelete) return@setOnClickListener
                Log.d("adapter_log", "onBindViewHolder: position : $position")
                setOnClickListener?.invoke(position)
            }
        }
    }
}