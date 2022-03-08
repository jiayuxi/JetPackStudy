package com.jiayx.jetpackstudy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.room.bean.StudentBean

/**
 *Created by yuxi_
on 2022/2/27
 */
class MyAdapter(private val context: Context, private var items: List<StudentBean>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

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
        }
    }

    override fun getItemCount(): Int = if (items != null) items.size else 0

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var number: TextView = itemView.findViewById(R.id.item_id)
        var name: TextView = itemView.findViewById(R.id.item_name)
        var age: TextView = itemView.findViewById(R.id.item_age)
    }
}