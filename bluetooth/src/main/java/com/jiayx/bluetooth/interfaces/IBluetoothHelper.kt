package com.jiayx.bluetooth.interfaces

import android.bluetooth.BluetoothDevice
import android.content.Context

/**
 * @desc 蓝牙辅助统一接口
 */
interface IBluetoothHelper {
    //获取本地蓝牙名称
    val name: String?

    //获取本地蓝牙地址
    val address: String?

    //蓝牙是否可用，即是否打开
    val isEnable: Boolean

    //获取以配对设备
    fun getBondedDevices(): Set<BluetoothDevice>?

    //获取已连接的设备
    fun getConnectedDevices()

    fun init(context: Context)
    fun open(): Boolean //打开蓝牙
    fun close(): Boolean //关闭蓝牙
    fun startDiscovery(): Boolean //搜索蓝牙

    fun stopDiscovery(): Boolean //停止搜索蓝牙
    fun createBond(device: BluetoothDevice?): Boolean //配对

    fun removeBond(device: BluetoothDevice?): Boolean //取消配对

    //
    fun connect(device: BluetoothDevice?): Boolean //连接设备
    fun disconnect(device: BluetoothDevice?): Boolean //断开设备

    fun destroy()
    fun isConnected(device: BluetoothDevice?): Boolean //是否连接
    fun setDiscoverableTimeout(timeout: Int): Boolean //设备可见时间
    fun setBTStateListener(btStateListener: IBTStateListener?) //蓝牙状态监听(开关监听)
    fun setBTScanListener(btScanListener: IBTScanListener?) //蓝牙搜索监听
    fun setBTBondListener(btBondListener: IBTBondListener?) //蓝牙绑定监听
    fun setBTConnectListener(btConnectListener: IBTConnectListener?) //设置连接监听
}