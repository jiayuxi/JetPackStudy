package com.jiayx.jetpackstudy.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.room.bean.StudentBean
import com.jiayx.jetpackstudy.ui.main.utils.transToString
import com.jiayx.jetpackstudy.ui.main.utils.transToTimeStamp

/**
 *Created by yuxi_
on 2022/2/27
 */
class RoomAdapter(private val context: Context, private var items: List<StudentBean>) :
    RecyclerView.Adapter<RoomAdapter.MyViewHolder>() {

    fun updateData(data: List<StudentBean>) {
        items = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.room_fragment_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        items?.let {
            val bean = it[position]
            holder.number.text = bean?.longId.toString()
            holder.name.text = bean?.name
            holder.age.text = bean?.age.toString()
            Log.d("model_log", "onBindViewHolder: time: ${transToString(bean?.timeLog)}")
            holder.time.text = transToString(bean?.timeLog)
            holder.time.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    Log.d(
                        "jia_itemClick",
                        "onBindViewHolder: position $position , name:${bean?.name}"
                    )
                }
                return@setOnTouchListener false
            }
        }
    }

    override fun getItemCount(): Int = if (items != null) items.size else 0

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var number: TextView = itemView.findViewById(R.id.item_id)
        var name: TextView = itemView.findViewById(R.id.item_name)
        var age: TextView = itemView.findViewById(R.id.item_age)
        var time: TextView = itemView.findViewById(R.id.item_time)
    }
}