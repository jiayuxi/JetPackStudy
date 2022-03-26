package com.jiayx.wlan.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseSoftInputAdapter<T : BaseSoftInputAdapter.Companion.BaseItem, VH : BaseSoftInputAdapter.Companion.ViewHolder>(
    @LayoutRes open var layout: Int,
    open var items: ArrayList<T?>,
) : RecyclerView.Adapter<VH>() {

    @Suppress("PropertyName")
    open val TAG: String = "LOG${javaClass.simpleName}"

    open var listener: ((position: Int) -> Unit)? = null
    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloadData(holder, items[position], position, payloads)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        bindData(holder, items[position], position)

    protected abstract fun bindData(holder: VH, item: T?, position: Int)

    protected abstract fun payloadData(
        holder: VH,
        item: T?,
        position: Int,
        payloads: MutableList<Any>
    )

    open var selection: Int = 0
        set(value) {
            if (value >= 0 && value < items.count()) {
                field = value
            } else if (value >= 0 && value >= items.count()) {
                field = value % itemCount
            } else if (value < 0) {
                field = value % itemCount + itemCount
            }
            notifyDataSetChanged()
        }

    open fun setData(items: ArrayList<T?>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int = items.size

    companion object {
        abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        abstract class BaseItem
    }
}