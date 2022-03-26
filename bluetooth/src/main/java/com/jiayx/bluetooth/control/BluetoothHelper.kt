package com.juxing.helmet.lib_bluetooth.control

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.jiayx.bluetooth.interfaces.*
import kotlin.collections.ArrayList

@SuppressLint("MissingPermission", "NewApi")
class BluetoothHelper :
    IBluetoothHelper {
    @Suppress("PrivatePropertyName")
    private val TAG = "BluetoothHelper"

    private var mContext: Context? = null

    private val btAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    //是否返回已连接的设备
    private var isBackConDev = false

    //
    private var mBluetoothA2dp: BluetoothA2dp? = null
    private var isA2dpComplete = false

    private var mBluetoothHeadset: BluetoothHeadset? = null
    private var isHeadsetComplete = false

    private var mBluetoothHidDevice: BluetoothHidDevice? = null
    private var isBluetoothHidDeviceComplete = false

    private val mFilter: IntentFilter by lazy {
        val temp = IntentFilter()
        //状态改变
        temp.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        //蓝牙开关状态
        temp.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        //蓝牙发现新设备(未配对)
        temp.addAction(BluetoothDevice.ACTION_FOUND)
        //蓝牙开始搜索
        temp.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        //蓝牙搜索结束
        temp.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        //设备配对状态改变
        temp.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        //设备建立连接
        temp.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        //设备断开连接
        temp.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        //BluetoothAdapter连接状态
        temp.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        //BluetoothHeadset连接状态
        temp.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
        //BluetoothA2dp连接状态
        temp.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
        return@lazy temp
    }

    //蓝牙状态监听
    private var mBTStateListener: IBTStateListener? = null

    //蓝牙搜索监听
    private var mBTScanListener: IBTScanListener? = null

    //蓝牙绑定监听
    private var mBTBondListener: IBTBondListener? = null

    //连接监听
    private var mBTConnectListener: IBTConnectListener? = null

    override fun init(context: Context) {
        mContext = context.applicationContext

        for (i in 0 until 22) {
            btAdapter.getProfileProxy(mContext, mProfileServiceListener, i)
        }
        context.applicationContext.registerReceiver(mBluetoothReceiver, mFilter)
    }

    /**
     * 关闭蓝牙
     */
    override fun close(): Boolean = btAdapter.disable()

    /**
     * 连接A2dp 与 HeadSet
     *
     * @param device           BluetoothDevice
     * @param bluetoothProfile BluetoothProfile
     * @Param btClass          Class
     */
    private fun connectA2dpAndHeadSet(
        btClass: Class<out BluetoothProfile>,
        bluetoothProfile: BluetoothProfile?,
        device: BluetoothDevice?,
    ): Boolean {
        //设置priority优先级
        setPriority(device, 100)
        try {
            //通过反射获取BluetoothA2dp中connect方法（hide的），进行连接。
            val connectMethod = btClass.getMethod("connect", BluetoothDevice::class.java)
            connectMethod.isAccessible = true
            connectMethod.invoke(bluetoothProfile, device)
            Log.d(TAG, String.format("connectA2dpAndHeadSet: btClass = %s, result = true", btClass))
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d(TAG, String.format("connectA2dpAndHeadSet: btClass = %s, result = false", btClass))
        return false
    }

    /**
     * 断开A2dp 与 HeadSet
     *
     * @param device BluetoothDevice
     */
    private fun disConnectA2dpAndHeadSet(
        btClass: Class<out BluetoothProfile>,
        bluetoothProfile: BluetoothProfile,
        device: BluetoothDevice?,
    ): Boolean {
        setPriority(device, 0)
        try {
            //通过反射获取BluetoothA2dp中connect方法（hide的），断开连接。
            val connectMethod = btClass.getMethod(
                "disconnect",
                BluetoothDevice::class.java
            )
            connectMethod.isAccessible = true
            connectMethod.invoke(bluetoothProfile, device)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 设置优先级
     * 优先级是必要的，否则可能导致连接或断开连接失败等问题
     *
     * @param device   BluetoothDevice
     * @param priority int 优先级
     */
    private fun setPriority(device: BluetoothDevice?, priority: Int) {
        if (mBluetoothA2dp == null) return
        try {
            //通过反射获取BluetoothA2dp中setPriority方法（hide的），设置优先级
            val connectMethod = BluetoothA2dp::class.java.getMethod(
                "setPriority",
                BluetoothDevice::class.java, Int::class.javaPrimitiveType
            )
            connectMethod.isAccessible = true
            connectMethod.invoke(mBluetoothA2dp, device, priority)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 打开蓝牙 ture-打开成功
     */
    override fun open(): Boolean = btAdapter.enable()

    /**
     * 搜索蓝牙
     */
    override fun startDiscovery(): Boolean {
        if (btAdapter.isDiscovering) {
            btAdapter.cancelDiscovery()
        }
        return btAdapter.startDiscovery()
    }

    /**
     * 停止搜索蓝牙
     */
    override fun stopDiscovery(): Boolean {
        return if (!btAdapter.isDiscovering) true else btAdapter.cancelDiscovery()
    }

    /**
     * 获取本地蓝牙名称
     */
    override val name: String?
        get() = btAdapter.name ?: null

    /**
     * 获取本地蓝牙地址
     */
    override val address: String?
        @SuppressLint("HardwareIds")
        get() = btAdapter.address

    /**
     * 蓝牙是否可用，即是否打开
     */
    override val isEnable: Boolean
        get() = btAdapter.isEnabled

    /**
     * 配对
     */
    override fun createBond(device: BluetoothDevice?): Boolean {
        return device?.createBond() ?: false
    }

    /**
     * 获取以配对设备
     */
    override fun getBondedDevices(): Set<BluetoothDevice>? {
        return btAdapter.bondedDevices
    }

    /**
     * 取消配对
     */
    override fun removeBond(device: BluetoothDevice?): Boolean {
        val btDeviceCls = BluetoothDevice::class.java
        return try {
            val removeBond = btDeviceCls.getMethod("removeBond")
            removeBond.isAccessible = true
            removeBond.invoke(device) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun connect(device: BluetoothDevice?): Boolean {
        var isConnect = false
        if (connectA2dpAndHeadSet(BluetoothHeadset::class.java, mBluetoothHeadset, device)) {
            isConnect = true
        }
        if (connectA2dpAndHeadSet(BluetoothHidDevice::class.java, mBluetoothHidDevice, device)) {
            isConnect = true
        }
        if (connectA2dpAndHeadSet(BluetoothA2dp::class.java, mBluetoothA2dp, device)) {
            isConnect = true
        }
        return isConnect
    }

    override fun disconnect(device: BluetoothDevice?): Boolean {
        var isDisconnect = false
        mBluetoothA2dp?.let { mBluetoothA2dp ->
            val devices = mBluetoothA2dp.connectedDevices
            if (devices != null && devices.contains(device)) {
                Log.d(TAG, "disconnect A2dp")
                isDisconnect = disConnectA2dpAndHeadSet(
                    BluetoothA2dp::class.java,
                    mBluetoothA2dp,
                    device
                )
            }
        }
        mBluetoothHeadset?.let { mBluetoothHeadset ->
            val devices = mBluetoothHeadset.connectedDevices
            if (devices?.contains(device) == true) {
                Log.d(TAG, "disconnect Headset")
                isDisconnect = disConnectA2dpAndHeadSet(
                    BluetoothHeadset::class.java,
                    mBluetoothHeadset,
                    device
                )
            }
        }
        mBluetoothHidDevice?.let { mBluetoothHidDevice ->
            val devices = mBluetoothHidDevice.connectedDevices
            if (devices?.contains(device) == true) {
                Log.d(TAG, "disconnect Headset")
                isDisconnect = disConnectA2dpAndHeadSet(
                    BluetoothHidDevice::class.java,
                    mBluetoothHidDevice,
                    device
                )
            }
        }
        return isDisconnect
    }

    override fun destroy() {
        mContext?.unregisterReceiver(mBluetoothReceiver)
        isA2dpComplete = false
        isHeadsetComplete = false
        isBluetoothHidDeviceComplete = false
        btAdapter.closeProfileProxy(BluetoothProfile.A2DP, mBluetoothA2dp)
        btAdapter.closeProfileProxy(BluetoothProfile.HEADSET, mBluetoothHeadset)
        btAdapter.closeProfileProxy(BluetoothProfile.HID_DEVICE, mBluetoothHidDevice)
    }

    override fun getConnectedDevices() {
        if (isBackConDev) return
        isBackConDev = true
        if (isA2dpComplete && isHeadsetComplete && isBluetoothHidDeviceComplete) {
            val devices: ArrayList<BluetoothDevice> = ArrayList()
            if (mBluetoothA2dp != null) {
                val deviceList = mBluetoothA2dp?.connectedDevices
                if (deviceList != null && deviceList.size > 0) {
                    devices.addAll(deviceList)
                }
            }
            if (mBluetoothHeadset != null) {
                val deviceList = mBluetoothHeadset?.connectedDevices
                if (deviceList != null && deviceList.size > 0) {
                    devices.addAll(deviceList)
                }
            }
            if (mBluetoothHidDevice != null) {
                val deviceList = mBluetoothHidDevice?.connectedDevices
                if (deviceList != null && deviceList.size > 0) {
                    devices.addAll(deviceList)
                }
            }
            mBTConnectListener?.onConnectedDevice(devices)
            isBackConDev = false
        }
    }

    override fun isConnected(device: BluetoothDevice?): Boolean {
        if (mBluetoothA2dp != null && mBluetoothA2dp?.getConnectionState(device) == BluetoothA2dp.STATE_CONNECTED) {
            Log.d(TAG, "isConnected name=" + device?.name)
            val bluetoothDeviceList = mBluetoothA2dp?.connectedDevices
            if (bluetoothDeviceList != null && bluetoothDeviceList.size > 0) {
                for (bluetoothDevice in bluetoothDeviceList) {
                    if (!TextUtils.isEmpty(device?.address) && device?.address == bluetoothDevice.address) {
                        return true
                    }
                }
            }
        }
        if (mBluetoothHeadset != null && mBluetoothHeadset?.getConnectionState(device) == BluetoothHeadset.STATE_CONNECTED) {
            Log.d(TAG, "isConnected name=" + device?.name)
            val bluetoothDeviceList = mBluetoothHeadset?.connectedDevices
            if (bluetoothDeviceList != null && bluetoothDeviceList.size > 0) {
                for (bluetoothDevice in bluetoothDeviceList) {
                    if (!TextUtils.isEmpty(device?.address) && device?.address == bluetoothDevice.address) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun setDiscoverableTimeout(timeout: Int): Boolean {
        try {
            //得到指定的类中的方法
            val setDiscoverableTimeout =
                BluetoothAdapter::class.java.getMethod(
                    "setDiscoverableTimeout",
                    Int::class.javaPrimitiveType
                )
            setDiscoverableTimeout.isAccessible = true
            val setScanMode = BluetoothAdapter::class.java.getMethod(
                "setScanMode",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            setScanMode.isAccessible = true
            setDiscoverableTimeout.invoke(btAdapter, timeout)
            setScanMode.invoke(
                btAdapter,
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,
                timeout
            )
            return true
        } catch (e: Exception) {
            Log.d(TAG, "setDiscoverableTimeout msg=" + e.message)
            e.printStackTrace()
        }
        return false
    }

    override fun setBTStateListener(btStateListener: IBTStateListener?) {
        mBTStateListener = btStateListener
    }

    override fun setBTScanListener(btScanListener: IBTScanListener?) {
        mBTScanListener = btScanListener
    }

    override fun setBTBondListener(btBondListener: IBTBondListener?) {
        mBTBondListener = btBondListener
    }

    override fun setBTConnectListener(btConnectListener: IBTConnectListener?) {
        mBTConnectListener = btConnectListener
    }

    private val mProfileServiceListener: BluetoothProfile.ServiceListener =
        object : BluetoothProfile.ServiceListener {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                Log.i(
                    TAG,
                    String.format("onServiceConnected profile=%d, proxy = %s", profile, proxy)
                )
                if (profile == BluetoothProfile.A2DP) {
                    mBluetoothA2dp = proxy as BluetoothA2dp
                    isA2dpComplete = true
                } else if (profile == BluetoothProfile.HEADSET) {
                    mBluetoothHeadset = proxy as BluetoothHeadset
                    isHeadsetComplete = true
                } else if (profile == BluetoothProfile.HID_DEVICE) {
                    mBluetoothHidDevice = proxy as BluetoothHidDevice
                    isBluetoothHidDeviceComplete = true
                }
                //
                if (isA2dpComplete
                    && isHeadsetComplete
                    && isBluetoothHidDeviceComplete
                    && isBackConDev
                    && mBTConnectListener != null
                ) {
                    val devices: MutableList<BluetoothDevice> = ArrayList()
                    if (mBluetoothA2dp != null) {
                        val deviceList = mBluetoothA2dp?.connectedDevices
                        if (deviceList != null && deviceList.size > 0) {
                            devices.addAll(deviceList)
                        }
                    }
                    if (mBluetoothHeadset != null) {
                        val deviceList = mBluetoothHeadset?.connectedDevices
                        if (deviceList != null && deviceList.size > 0) {
                            devices.addAll(deviceList)
                        }
                    }
                    if (mBluetoothHidDevice != null) {
                        val deviceList = mBluetoothHidDevice?.connectedDevices
                        if (deviceList != null && deviceList.size > 0) {
                            devices.addAll(deviceList)
                        }
                    }
                    mBTConnectListener?.onConnectedDevice(devices)
                }
            }

            override fun onServiceDisconnected(profile: Int) {
                Log.i(TAG, "onServiceDisconnected profile=$profile")
                if (profile == BluetoothProfile.A2DP) {
                    mBluetoothA2dp = null
                } else if (profile == BluetoothProfile.HEADSET) {
                    mBluetoothHeadset = null
                } else if (profile == BluetoothProfile.HID_DEVICE) {
                    mBluetoothHidDevice = null
                }
            }
        }
    private val mBluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val dev: BluetoothDevice?
            val state: Int
            if (action == null) {
                return
            }
            when (action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                    mBTStateListener?.onStateChange(state)
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.i(TAG, "蓝牙开始搜索")
                    mBTScanListener?.onScanStart()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.i(TAG, "蓝牙扫描结束")
                    mBTScanListener?.onScanStop(null)
                }
                BluetoothDevice.ACTION_FOUND -> {
                    dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    //                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,(short)0);//信号强度
                    mBTScanListener?.onFindDevice(dev)
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    mBTBondListener?.onBondStateChange(dev)
                    Log.i(TAG, "设备配对状态改变：" + dev?.bondState)
                }
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.i(TAG, "设备建立连接：" + dev?.bondState)
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.i(TAG, "设备断开连接：" + dev?.bondState)
                }
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                    dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.i(
                        TAG,
                        "Adapter STATE: " + intent.getIntExtra(
                            BluetoothAdapter.EXTRA_CONNECTION_STATE,
                            0
                        )
                    )
                    Log.i(TAG, "BluetoothDevice: " + dev?.name + ", " + dev?.address)
                }
                BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> {
                    dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.i(
                        TAG,
                        "Headset STATE: " + intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, 0)
                    )
                    Log.i(TAG, "BluetoothDevice: " + dev?.name + ", " + dev?.address)
                    when (intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)) {
                        BluetoothProfile.STATE_CONNECTING -> if (mBTConnectListener != null) {
                            mBTConnectListener?.onConnecting(dev)
                        }
                        BluetoothProfile.STATE_CONNECTED -> if (mBTConnectListener != null) {
                            mBTConnectListener?.onConnected(dev)
                        }
                        BluetoothProfile.STATE_DISCONNECTED -> if (mBTConnectListener != null) {
                            mBTConnectListener?.onDisConnect(dev)
                        }
                        BluetoothProfile.STATE_DISCONNECTING -> if (mBTConnectListener != null) {
                            mBTConnectListener?.onDisConnecting(dev)
                        }
                    }
                }
                BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> {
                    dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    when (intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1)) {
                        BluetoothA2dp.STATE_CONNECTING -> {
                            Log.i(TAG, "A2dp device: " + dev?.name + " connecting")
                            mBTConnectListener?.onConnecting(dev)
                        }
                        BluetoothA2dp.STATE_CONNECTED -> {
                            Log.i(TAG, "A2dp device: " + dev?.name + " connected")
                            mBTConnectListener?.onConnected(dev)
                        }
                        BluetoothA2dp.STATE_DISCONNECTING -> {
                            Log.i(TAG, "A2dp device: " + dev?.name + " disconnecting")
                            mBTConnectListener?.onDisConnecting(dev)
                        }
                        BluetoothA2dp.STATE_DISCONNECTED -> {
                            Log.i(TAG, "A2dp device: " + dev?.name + " disconnected")
                            mBTConnectListener?.onDisConnect(dev)
                        }
                    }
                }
            }
        }
    }
}