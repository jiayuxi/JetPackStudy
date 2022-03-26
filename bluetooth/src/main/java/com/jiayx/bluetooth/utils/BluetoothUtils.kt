package com.jiayx.bluetooth.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.media.AudioManager
import java.lang.reflect.InvocationTargetException

private val mBluetoothAdapter: BluetoothAdapter by lazy {
    BluetoothAdapter.getDefaultAdapter()
}

/**
 * 检测设备是否支持蓝牙
 */
fun checkBluetoothEnable(): Boolean {
    return mBluetoothAdapter == null
}

/**
 * 判断当前蓝牙是否打开
 */
fun checkBluetoothStateEnable(): Boolean {
    return mBluetoothAdapter.isEnabled
}

/**
 * 获取蓝牙耳机连接状态
 */
private fun isWiredHeadsetConnected(context: Context): Boolean {
    try {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.isWiredHeadsetOn
    } catch (e: Exception) {
    }
    return false
}

/**
 * 判断蓝牙耳机是否已连接
 */
fun hasBluetoothAudioDevice(): Boolean {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    var a2dp = false
    var headset = false
    try {
        a2dp =
            adapter.getProfileConnectionState(BluetoothProfile.A2DP) != BluetoothProfile.STATE_DISCONNECTED
        headset =
            adapter.getProfileConnectionState(BluetoothProfile.HEADSET) != BluetoothProfile.STATE_DISCONNECTED
    } catch (e: Throwable) {
    }
    return a2dp || headset
}

/**
 * 获取到已配对成功蓝牙设备
 */
fun fetchAlReadyConnection() {
    mBluetoothAdapter?.let {
        val devices = it.bondedDevices
        for (device in devices) {
        }
    }
}

fun getConnectedBtDevice(): String? {
    //获取蓝牙适配器
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    //得到已匹配的蓝牙设备列表
    val bondedDevices = bluetoothAdapter.bondedDevices
    if (bondedDevices != null && bondedDevices.size > 0) {
        for (bondedDevice in bondedDevices) {
            try {
                //使用反射调用被隐藏的方法
                val isConnectedMethod =
                    BluetoothDevice::class.java.getDeclaredMethod(
                        "isConnected"
                    )
                isConnectedMethod.isAccessible = true
                val isConnected =
                    isConnectedMethod.invoke(bondedDevice) as Boolean
                if (isConnected) {
                    return bondedDevice.name
                }
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }
    return null
}