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
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.binding.BindingViewHolder
import com.jiayx.jetpackstudy.databinding.PagingActivityBinding
import com.jiayx.jetpackstudy.databinding.PagingFragmentItemBinding
import com.jiayx.jetpackstudy.databinding.RoomFragmentItemBinding
import com.jiayx.jetpackstudy.paging.BasePagingAdapterKtx
import com.jiayx.jetpackstudy.room.bean.Person

/**
 *Created by yuxi_
on 2022/2/27
 */
class PagingAdapter(private val context: Context) : BasePagingAdapterKtx<Person>(itemCallback(
    areItemsTheSame = { old, new ->
        old.id == new.id
    },
    areContentsTheSame = { old, new ->
        old.name == new.name && old.isSelect == new.isSelect
    }, getChangePayload = { old, new ->
        if (old.isSelect != new.isSelect) {
            new.isSelect
        } else {
            null
        }
    }
)) {
    var isDelete: Boolean = false
    override fun getItemLayout(position: Int): Int {
        return R.layout.paging_fragment_item
    }

    override fun bindData(helper: ItemHelper, data: Person?, payloads: MutableList<Any>?) {
        data?.let {
            helper.setVisibility(R.id.checkbox, if (isDelete) View.VISIBLE else View.GONE)
            if (payloads?.isNotEmpty() == true) {
                helper.setCheckedTextView(R.id.checkbox, it.isSelect)
            } else {
                helper.setText(R.id.item_name, it.name)
                    .setCheckedTextView(R.id.checkbox, it.isSelect)
            }
        }
    }
}