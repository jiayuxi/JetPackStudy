package com.jiayx.wlan.adapter

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseWifiListAdapter<T : BaseWifiListAdapter.Companion.BaseItem, VH : BaseWifiListAdapter.Companion.ViewHolder>(
    @LayoutRes open var layout: Int,
    open var items: ArrayList<T>,
    private val rowCount: Int,
    private val columnCount: Int,
) : RecyclerView.Adapter<VH>() {

    @Suppress("PropertyName")
    open val TAG: String = "LOG${javaClass.simpleName}"
    var pageNum = 0
    var pageCount = 0
        get() {
            // 确认排满的页面数量
            var temp1 = items.size / (rowCount * columnCount)
            // 确认最后一页是否为非排满
            val temp2 = items.size % (rowCount * columnCount)
            // 当最后一页为非排满时，总页数 = 排满页数 + 1，否则为排满页数
            if (temp2 > 0) temp1++
            return temp1
        }
    var onFrontPageListener: ((position: Int) -> Unit)? = null
    var onNextPageListener: ((position: Int) -> Unit)? = null
    var onPageChangeListener: ((position: Int) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(pageNum)
        }

    open var listener: ((position: Int) -> Unit)? = null
    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloadData(
                holder,
                items[pageNum * rowCount * columnCount + position],
                position,
                payloads
            )
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindData(holder, items[pageNum * rowCount * columnCount + position], position)
    }

    protected abstract fun bindData(holder: VH, item: T?, position: Int)
    protected abstract fun payloadData(
        holder: VH,
        item: T?,
        position: Int,
        payloads: MutableList<Any>
    )

    override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int =
        minOf(rowCount * columnCount, items.size - (pageNum * rowCount * columnCount))

    private fun hasFrontPage(): Boolean = pageNum > 0

    private fun hasNextPage(): Boolean = items.size > ((pageNum + 1) * columnCount * rowCount)

    fun onFrontPage() {
        if (hasFrontPage()) {
            pageNum -= 1
            onFrontPageListener?.invoke(pageNum)
            onPageChangeListener?.invoke(pageNum)
            selection = 0
        }
    }

    fun onNextPage() {
        Log.e("wanghao", "hasNextPage() ${hasNextPage()}  $pageNum")
        if (hasNextPage()) {
            pageNum += 1
            onNextPageListener?.invoke(pageNum)
            onPageChangeListener?.invoke(pageNum)
            selection = 0
        }
    }

    var selection: Int = 0
        set(value) {
            // 得到item在列表中的index
            val index = pageNum * rowCount * columnCount + value
            if((pageNum + 1) > pageCount){
                onFrontPage()
                return
            }
            // 已经划到最左边
            if (index == -1) return
            // 已经划到最右边
            if (index == items.size || value >= items.size) return
            // 计算当前的selection
            field = index % (rowCount * columnCount)
            // 计算当前的pageNum
            pageNum = index / (rowCount * columnCount)
            // 触发翻到下一页
            if (value > field && value == rowCount * columnCount) {
                onNextPageListener?.invoke(value)
                onPageChangeListener?.invoke(pageNum)
            }
            // 触发翻到前一页
            if (value < field && field == rowCount * columnCount - 1) {
                onFrontPageListener?.invoke(value)
                onPageChangeListener?.invoke(pageNum)
            }
            notifyDataSetChanged()
        }

    open fun setData(items: ArrayList<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    open fun setData(items: ArrayList<T>, value: Int) {
        this.items = items
        notifyDataSetChanged()
    }

    open fun updatePage(value: Int): Int {
        val temp2 = items.size % (rowCount * columnCount)
        Log.d("scan_result_log", "当前页数: ${pageNum} , 总页数: $pageCount , 余数 ：$temp2 , 是否 ： ${(value > temp2 && temp2 != 0) && ((pageNum + 1) == pageCount)}" )
        if ((value > temp2 && temp2 != 0) && ((pageNum + 1) == pageCount)) {
            Log.d("scan_result_log", "返回当前选中的：${temp2 - 1}")
            return temp2 - 1
        } else if ((pageNum + 1) > pageCount) {
            onFrontPage()
            return 0
        }
        return value
    }

    companion object {
        abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        abstract class BaseItem {
        }
    }
}