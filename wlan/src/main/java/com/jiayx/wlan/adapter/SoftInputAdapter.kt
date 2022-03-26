package com.jiayx.wlan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jiayx.wlan.R
import com.jiayx.wlan.softInput.SHIFT_KEY

class SoftInputAdapter(
    context: Context, items: ArrayList<Item?>) :
    BaseSoftInputAdapter<SoftInputAdapter.Companion.Item, SoftInputAdapter.Companion.ViewHolder>(
        R.layout.lib_wifi_softinput_item_layout,
        items
    ) {

    var ifShift: Boolean = false

    override fun bindData(holder: ViewHolder, item: Item?, position: Int) {
        val isSelected = selection == position
        item?.let {
            if (it.shiftKeyword == "") {
                holder.image.visibility = View.VISIBLE
                if (it.keyType == SHIFT_KEY) {
                    holder.image.setImageResource(if (ifShift) it.selectRes else it.res)
                } else {
                    holder.image.setImageResource(it.res)
                }
                holder.name.visibility = View.GONE
            } else {
                holder.image.visibility = View.GONE
                holder.image.setImageResource(0)
                holder.name.visibility = View.VISIBLE
                holder.name.text = if (ifShift) it.shiftKeyword else it.keyword
            }


        }
        holder.selectItem.isSelected = isSelected
    }

    override fun payloadData(
        holder: ViewHolder,
        item: Item?,
        position: Int,
        payloads: MutableList<Any>
    ) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener { listener?.invoke(position) }
        return ViewHolder(view)
    }

    companion object {
        open class Item(
            var keyword: String,
            var shiftKeyword: String,
            var columnsSpan: Int,
            var res: Int,
            var selectRes: Int,
            var keyType: Int
        ) : BaseSoftInputAdapter.Companion.BaseItem()

        open class ViewHolder(itemView: View) : BaseSoftInputAdapter.Companion.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.softInput_item_text)
            var selectItem: ConstraintLayout = itemView.findViewById(R.id.softInput_item_id)
            var image: ImageView = itemView.findViewById(R.id.softInput_item_image)
        }
    }
}