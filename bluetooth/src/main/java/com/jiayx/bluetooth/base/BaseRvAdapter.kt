package com.jiayx.bluetooth.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRvAdapter<T : BaseRvAdapter.Companion.BaseItem, VH : BaseRvAdapter.Companion.ViewHolder>(
    @LayoutRes open var layout: Int,
    open var items: ArrayList<T?>,
) : RecyclerView.Adapter<VH>() {

    @Suppress("PropertyName")
    open val TAG: String = "LOG${javaClass.simpleName}"

    open var listener: ((position: Int) -> Unit)? = null

    override fun onBindViewHolder(holder: VH, position: Int) = bindData(holder, items[position], position)

    protected abstract fun bindData(holder: VH, item: T?, position: Int)

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

/**
 * 实现体模板
 */
private class BaseRvAdapterImpl(items: ArrayList<Item?>) :
    BaseRvAdapter<BaseRvAdapterImpl.Companion.Item, BaseRvAdapterImpl.Companion.ViewHolder>(
        0, //TODO R.layout.xxx
        items
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener { listener?.invoke(position) }
        return ViewHolder(view)
    }

    override fun bindData(holder: ViewHolder, item: Item?, position: Int) {}


    companion object {
        class Item : BaseRvAdapter.Companion.BaseItem()
        class ViewHolder(itemView: View) : BaseRvAdapter.Companion.ViewHolder(itemView) {
            var ctvTitle = itemView.findViewById(
                0//TODO R.id.xxx
            ) as CheckedTextView
        }
    }
}