package com.jiayx.bluetooth.base

import android.util.Log
import androidx.annotation.LayoutRes

abstract class BaseGridRlvAdapter<T : BaseRvAdapter.Companion.BaseItem, VH : BaseRvAdapter.Companion.ViewHolder>(
    @LayoutRes
    override var layout: Int,
    override var items: ArrayList<T?>,
    private val rowCount: Int,
    private val columnCount: Int,
) : BaseRvAdapter<T, VH>(layout, items) {

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

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindData(holder, items[pageNum * rowCount * columnCount + position], position)
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int =
        minOf(rowCount * columnCount, items.size - (pageNum * rowCount * columnCount))

    private fun hasFrontPage(): Boolean = pageNum > 0

    private fun hasNextPage(): Boolean = items.size > ((pageNum+1) * columnCount * rowCount)

    fun onFrontPage() {
        if (hasFrontPage()) {
            pageNum -= 1
            onFrontPageListener?.invoke(pageNum)
            onPageChangeListener?.invoke(pageNum)
            selection = 0
        }
    }

    fun onNextPage() {
        Log.e("wanghao","hasNextPage() ${hasNextPage()}  $pageNum")
        if (hasNextPage()) {
            pageNum += 1
            onNextPageListener?.invoke(pageNum)
            onPageChangeListener?.invoke(pageNum)
            selection = 0
        }
    }

    override var selection: Int = 0
        set(value) {
            // 得到item在列表中的index
            val index = pageNum * rowCount * columnCount + value
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
}

///**
// * 实现体模板
// */
//private class BaseGridRlvAdapterImpl(
//    context: Context,
//    items: ArrayList<Item?>,
//    val rowCount: Int,
//    val columnCount: Int,
//) : BaseGridRlvAdapter<BaseGridRlvAdapterImpl.Companion.Item, BaseGridRlvAdapterImpl.Companion.ViewHolder>(
//    R.layout.module_settings_item_basic_app_market,
//    items,
//    rowCount,
//    columnCount
//) {
//
//    private var selected: Int = context.resources.getColor(R.color.blue, null)
//    private var unselect: Int = context.resources.getColor(R.color.colorWhite, null)
//
//    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
//        view.setOnClickListener { listener?.invoke(position) }
//        return ViewHolder(view)
//    }
//
//    override fun bindData(holder: ViewHolder, item: Item?, position: Int) {
//        //
//        val isChecked = selection == position
//        // 设置跑马灯效果
//        holder.ctvTitle.isSelected = isChecked
//        // 设置选中状态
//        holder.ctvTitle.isChecked = isChecked
//        // 设置文本
//        holder.ctvTitle.text = item?.title ?: ""
//        // 设置文本选中颜色
//        holder.ctvTitle.setTextColor(if (isChecked) selected else unselect)
//        // 设置图标大小
//        item?.drawable?.setBounds(0, 0, 180, 180)
//        // 设置图标
//        holder.ctvTitle.setCompoundDrawables(null, item?.drawable, null, null)
//    }
//
//    companion object {
//        open class Item constructor(val title: String, var drawable: Drawable?, var packageName: String) : BaseRvAdapter.Companion.BaseItem()
//        open class ViewHolder(itemView: View) : BaseRvAdapter.Companion.ViewHolder(itemView) {
//            var ctvTitle = itemView.findViewById(R.id.ctvTitle) as CheckedTextView
//        }
//    }
//}