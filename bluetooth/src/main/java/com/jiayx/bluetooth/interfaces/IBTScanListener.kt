package com.jiayx.bluetooth.interfaces

import android.bluetooth.BluetoothDevice

/**
 * @date 2019/7/23
 * @desc 蓝牙搜索监听
 */
interface IBTScanListener {
    /**
     * 搜索开始
     */
    fun onScanStart()

    /**
     * 搜索结束
     *
     * @param deviceList
     */
    fun onScanStop(deviceList: List<BluetoothDevice>?)

    /**
     * 发现新设备
     * @param device
     */
    fun onFindDevice(device: BluetoothDevice?)
}