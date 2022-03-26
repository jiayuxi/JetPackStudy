package com.jiayx.bluetooth.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.jiayx.bluetooth.utils.getConnectedBtDevice

class BluetoothReceiver : BroadcastReceiver() {
    /**
     * 蓝牙开关状态
     * int STATE_OFF = 10; //蓝牙关闭
     * int STATE_ON = 12; //蓝牙打开
     * int STATE_TURNING_OFF = 13; //蓝牙正在关闭
     * int STATE_TURNING_ON = 11; //蓝牙正在打开
     */
    open var bluetoothStateListener: ((state: Int) -> Unit)? = null

    open var bluetoothConnectListener: ((connect: String?) -> Unit)? = null

    companion object {
        fun registerIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED) // 蓝牙状态改变
                addAction("android.bluetooth.BluetoothAdapter.STATE_OFF") // 本地蓝牙适配器已关闭
                addAction("android.bluetooth.BluetoothAdapter.STATE_ON") // 本地蓝牙适配器已打开，可以使用
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED) // 已和远程设备建立 ACL 连接
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED) // 与远程设备 ACL 断开连接
                priority = Int.MAX_VALUE
            }
            return intentFilter
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        action ?: return
        when (action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> { // 监听蓝牙状态
                when (val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        "STATE_TURNING_ON 蓝牙开启中"
                    }
                    BluetoothAdapter.STATE_ON -> {// 蓝牙开启
                        "STATE_ON 蓝牙开启"
                        bluetoothStateListener?.invoke(BluetoothAdapter.STATE_ON)
                    }
                    /* BluetoothAdapter.STATE_CONNECTING -> {
                         "STATE_CONNECTING 蓝牙连接中"
                     }
                     BluetoothAdapter.STATE_CONNECTED -> {
                         "STATE_CONNECTED 蓝牙已连接"
                     }
                     BluetoothAdapter.STATE_DISCONNECTING -> {
                         "STATE_DISCONNECTING 蓝牙断开中"
                     }
                     BluetoothAdapter.STATE_DISCONNECTED -> {
                         "STATE_DISCONNECTED 蓝牙已断开"
                     }
                     BluetoothAdapter.STATE_TURNING_OFF -> {
                         "STATE_TURNING_OFF 蓝牙关闭中"
                     }*/
                    BluetoothAdapter.STATE_OFF -> { // 蓝牙关闭
                        bluetoothStateListener?.invoke(BluetoothAdapter.STATE_OFF)
                    }
                    else -> "ACTION_STATE_CHANGED EXTRA_STATE $state"
                }
            }
            BluetoothDevice.ACTION_ACL_CONNECTED -> { // 蓝牙已连接
                Log.e("HLQ", "----> ACTION_ACL_CONNECTED 蓝牙已连接 ") // 蓝牙已打开 且 已连接
                Log.e("HLQ", "----> 蓝牙已打开且已连接")
                Log.e("HLQ", "----> 输出已配对成功蓝牙列表")
                "----> 当前连接蓝牙名称：${getConnectedBtDevice()}"
                bluetoothConnectListener?.invoke(getConnectedBtDevice())
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> { // 蓝牙已断开
                bluetoothConnectListener?.invoke(null)
            }
            else -> "action $action"
        }
    }
}