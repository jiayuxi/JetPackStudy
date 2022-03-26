package com.jiayx.wlan.adapter

import android.content.Context
import android.net.wifi.WifiManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jiayx.wlan.R

class WifiListAdapter(
    context: Context?, items: ArrayList<WifiItem>, val rowCount: Int,
    val columnCount: Int,
) :
    BaseWifiListAdapter<WifiListAdapter.Companion.WifiItem, WifiListAdapter.Companion.ViewHolder>(
        R.layout.lib_wifi_item_wifi,
        items,
        rowCount,
        columnCount,
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener { listener?.invoke(position) }
        return ViewHolder(view)
    }

    override fun bindData(holder: ViewHolder, item: WifiItem?, position: Int) {
        holder.numberImage.setImageResource(numberImageRes(position % (rowCount * columnCount) + 1))
        val isSelected = selection == position
        // 设置跑马灯效果
        holder.itemBg.isSelected = isSelected
        item?.let { wifiItem ->
            holder.wifiLevelImage.setImageResource(resId(wifiItem.level, wifiItem.capabilities))
            holder.wifiNetworkState.text = wifiItem.networkState
            holder.wifiSSIDText.text = wifiItem.ssid
        }
    }

    override fun payloadData(
        holder: ViewHolder,
        item: WifiItem?,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.elementAtOrNull(0) == "wlan") {
            item?.let { wifiItem ->
                holder.wifiNetworkState.text = wifiItem.networkState
            }
        }
    }

    companion object {
        open class WifiItem(
            var ssid: String,
            var level: Int,
            var bssid: String?,
            var capabilities: String,
            var frequency: Int,
            var isSave: Boolean,
            var networkState: String?,
            var number: Int
        ) : BaseWifiListAdapter.Companion.BaseItem()

        open class ViewHolder(itemView: View) :
            BaseWifiListAdapter.Companion.ViewHolder(itemView) {
            val numberImage: ImageView = itemView.findViewById(R.id.lib_wifi_item_wifi_item_number)
            val wifiLevelImage: ImageView =
                itemView.findViewById(R.id.lib_wifi_item_wifi_item_level)
            val wifiSSIDText: TextView = itemView.findViewById(R.id.lib_wifi_item_wifi_item_ssid)
            val wifiNetworkState: TextView =
                itemView.findViewById(R.id.lib_wifi_item_wifi_item_networkState)
            val itemBg: ConstraintLayout = itemView.findViewById(R.id.lib_wifi_item_wifi_root)
        }
    }

    private fun resId(level: Int, capabilities: String): Int {
        return when (WifiManager.calculateSignalLevel(level, 5)) {
            0 -> if (getSecurityMode(capabilities)) {
                R.mipmap.lib_wifi_level_lock_0
            } else {
                R.mipmap.lib_wifi_level_0
            }
            1 -> if (getSecurityMode(capabilities)) {
                R.mipmap.lib_wifi_level_lock_1
            } else {
                R.mipmap.lib_wifi_level_1
            }
            2 -> if (getSecurityMode(capabilities)) {
                R.mipmap.lib_wifi_level_lock_2
            } else {
                R.mipmap.lib_wifi_level_2
            }
            3 -> if (getSecurityMode(capabilities)) {
                R.mipmap.lib_wifi_level_lock_3
            } else {
                R.mipmap.lib_wifi_level_3
            }
            4 -> if (getSecurityMode(capabilities)) {
                R.mipmap.lib_wifi_level_lock_4
            } else {
                R.mipmap.lib_wifi_level_4
            }
            else -> if (getSecurityMode(capabilities)) {
                R.mipmap.lib_wifi_level_lock_4
            } else {
                R.mipmap.lib_wifi_level_4
            }
        }
    }

    private fun numberImageRes(number: Int): Int {
        return when (number) {
            1 -> R.mipmap.lib_wifi_number_1
            2 -> R.mipmap.lib_wifi_number_2
            3 -> R.mipmap.lib_wifi_number_3
            4 -> R.mipmap.lib_wifi_number_4
            5 -> R.mipmap.lib_wifi_number_5
            6 -> R.mipmap.lib_wifi_number_6
            else -> R.mipmap.lib_wifi_number_1
        }
    }

    private fun getSecurityMode(capabilities: String): Boolean {
        //获取wifi加密类型
        return (capabilities.contains("WEP") || capabilities.contains("wep")
                || capabilities.contains("WPA") || capabilities.contains("wpa")
                || capabilities.contains("WPA2") || capabilities.contains("wpa2"))
    }
}