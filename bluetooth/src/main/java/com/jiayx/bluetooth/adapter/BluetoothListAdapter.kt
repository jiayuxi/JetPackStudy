package com.jiayx.bluetooth.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jiayx.bluetooth.R
import com.jiayx.bluetooth.base.BaseGridRlvAdapter
import com.jiayx.bluetooth.base.BaseRvAdapter
import com.jiayx.bluetooth.const.State

@SuppressLint("MissingPermission")
class BluetoothListAdapter constructor(
    items: ArrayList<Item?>,
    val rowCount: Int,
    val columnCount: Int,
) : BaseGridRlvAdapter<BluetoothListAdapter.Companion.Item, BluetoothListAdapter.Companion.ViewHolder>(
    R.layout.lib_bluetooth_item_bluetooth_list,
    items,
    rowCount,
    columnCount
) {
    var mItemClickListener: ((position: Int) -> Unit)? = null

    fun addData(item: Item) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun add(index: Int, item: Item) {
        items.add(index, item)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun getData() = items

    fun remove(o: Item?) {
        items.remove(o)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        view.setOnClickListener { mItemClickListener?.invoke(position) }
        return ViewHolder(view)
    }

    companion object {
        class Item constructor(
            var device: BluetoothDevice?,
            var state: Int,
        ) : BaseRvAdapter.Companion.BaseItem()

        class ViewHolder(itemView: View) : BaseRvAdapter.Companion.ViewHolder(itemView) {
            val numberImage: ImageView = itemView.findViewById(R.id.imgNumber)
            var imgStyleMajor = itemView.findViewById(R.id.imgStyleMajor) as ImageView
            var tvName = itemView.findViewById(R.id.tvName) as TextView
            var tvStatus = itemView.findViewById(R.id.tvStatus) as TextView
            var parent = itemView.findViewById(R.id.parent) as ConstraintLayout
        }
    }

    override fun bindData(holder: ViewHolder, item: Item?, position: Int) {
        holder.numberImage.setImageResource(numberImageRes(position % (rowCount * columnCount) + 1))
        holder.tvName.text = item?.device?.name?.let {
            item.device?.name
        } ?: kotlin.run {
            item?.device?.address
        }
        //
        holder.parent.isSelected = selection == position
        //
        when (item?.state) {
            State.STATE_UNCONNECT, State.STATE_BOND_NONE -> holder.tvStatus.text = ""
            State.STATE_BONDING -> holder.tvStatus.text = "配对中"
            State.STATE_BONDED -> holder.tvStatus.text = "已配对"
            State.STATE_CONNECTING -> holder.tvStatus.text = "连接中"
            State.STATE_CONNECTED -> holder.tvStatus.text = "已连接"
            State.STATE_DISCONNECTING -> holder.tvStatus.text = "断开中"
            State.STATE_DISCONNECTED -> holder.tvStatus.text = "已保存"
            else -> holder.tvStatus.text = "未知"
        }
        when (item?.device?.bluetoothClass?.majorDeviceClass) {
            BluetoothClass.Device.Major.AUDIO_VIDEO -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_headset_24)
            BluetoothClass.Device.Major.COMPUTER -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_computer_24)
            BluetoothClass.Device.Major.HEALTH -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_healing_24)
            BluetoothClass.Device.Major.IMAGING -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_camera_alt_24)
            BluetoothClass.Device.Major.MISC -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_mic_24)
            BluetoothClass.Device.Major.NETWORKING -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_network_24)
            BluetoothClass.Device.Major.PERIPHERAL -> {
                if (item.device?.name?.contains("keyboard", ignoreCase = true) == true) {
                    holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_keyboard_24)
                } else {
                    holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_mouse_24)
                }
            }
            BluetoothClass.Device.Major.PHONE -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_phone_android_24)
            BluetoothClass.Device.Major.TOY -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_toys_24)
            BluetoothClass.Device.Major.UNCATEGORIZED -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_bluetooth_24)
            BluetoothClass.Device.Major.WEARABLE -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_watch_24)
            else -> holder.imgStyleMajor.setImageResource(R.drawable.lib_bluetooth_ic_baseline_bluetooth_24)
        }
    }

    private fun numberImageRes(number: Int): Int {
        return when (number) {
            1 -> R.mipmap.lib_bluetooth_number_1
            2 -> R.mipmap.lib_bluetooth_number_2
            3 -> R.mipmap.lib_bluetooth_number_3
            4 -> R.mipmap.lib_bluetooth_number_4
            5 -> R.mipmap.lib_bluetooth_number_5
            6 -> R.mipmap.lib_bluetooth_number_6
            else -> R.mipmap.lib_bluetooth_number_1
        }
    }
}