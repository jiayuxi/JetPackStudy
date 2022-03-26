package com.jiayx.bluetooth.interfaces

import android.bluetooth.BluetoothDevice

/**
 * @desc 蓝牙连接监听
 */
interface IBTConnectListener {
    fun onConnecting(bluetoothDevice: BluetoothDevice?) //连接中
    fun onConnected(bluetoothDevice: BluetoothDevice?) //连接成功
    fun onDisConnecting(bluetoothDevice: BluetoothDevice?) //断开中
    fun onDisConnect(bluetoothDevice: BluetoothDevice?) //断开
    fun onConnectedDevice(devices: MutableList<BluetoothDevice>) //已连接的设备
}