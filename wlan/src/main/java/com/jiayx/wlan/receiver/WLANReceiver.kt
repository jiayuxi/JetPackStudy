package com.jiayx.wlan.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.jiayx.wlan.wifi.WiFiManager

class WLANReceiver : BroadcastReceiver() {

    open var wlanLevelListener: ((level: Int) -> Unit)? = null
    open var wlanStateListener: ((state: Int) -> Unit)? = null
    open var wlanConnectListener: ((state: String?) -> Unit)? = null

    companion object {
        fun registerIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.apply {
                addAction(WifiManager.RSSI_CHANGED_ACTION)
                addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
                addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            }
            return intentFilter
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        action ?: return
        when (action) {
            WifiManager.RSSI_CHANGED_ACTION -> {//信号强度的变化
                val wifiConnected = WiFiManager.getInstance(context).isWifiConnected
                if (!wifiConnected) return
                val mWifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                mWifiManager?.let {
                    val connectionInfo = it.connectionInfo
                    connectionInfo?.let { info ->
                        wlanLevelListener?.invoke(WifiManager.calculateSignalLevel(info.rssi, 5))
                    } ?: kotlin.run {
                        wlanLevelListener?.invoke(0)
                    }
                } ?: kotlin.run {
                    wlanLevelListener?.invoke(0)
                }
            }
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {// wifi 开关状态
                when (intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_DISABLED
                )) {
                    WifiManager.WIFI_STATE_DISABLED -> {
                        wlanStateListener?.invoke(WifiManager.WIFI_STATE_DISABLED)
                    }
                    WifiManager.WIFI_STATE_ENABLED -> {
                        wlanStateListener?.invoke(WifiManager.WIFI_STATE_ENABLED)
                    }
                }
            }
            WifiManager.NETWORK_STATE_CHANGED_ACTION -> { // 连接状态
                val info: NetworkInfo? = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)
                when (info?.state) {
                    NetworkInfo.State.DISCONNECTED -> {
                        wlanConnectListener?.invoke("未连接")
                    }
                    NetworkInfo.State.CONNECTED -> {
                        val wifiManager =
                            context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                        val wifiInfo: WifiInfo = wifiManager.connectionInfo
                        val ssid = wifiInfo.ssid
                        wlanConnectListener?.invoke(ssid)
                    }
                    else -> {}
                }
            }
        }
    }
}