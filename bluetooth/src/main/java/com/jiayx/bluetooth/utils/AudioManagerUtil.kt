package com.jiayx.bluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.util.Log


class AudioManagerUtil {
    private var mAudioManager: AudioManager? = null
    private var mContext: Context? = null
    open var audioManageChangeListener: ((state: Int, flag: Boolean) -> Unit)? = null

    companion object {
       const val TAG: String = "bluetooth_log"
        const val bluetoothState: Int = 1
        const val WiredHeadsetOnState: Int = 2

        val INSTANCE: AudioManagerUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AudioManagerUtil()
        }
    }

    /**
     * 初始化
     */
    fun init(context: Context) {
        mContext = context
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    /**
     * 判断是否有耳机连接
     */

    fun isWiredHeadsetOn(): Boolean {
        mAudioManager?.let {
            return it.isWiredHeadsetOn
        } ?: kotlin.run {
            return false
        }
    }

    /**
     * 切换 音频通道
     */
    fun changeAudioMode() {
        if (isWiredHeadsetOn()) {
            changeToHeadset()
        } else if (isBluetoothAccess()) {
            changeToBluetooth()
        }
    }

    /**
     * 是否有耳机接入
     */
    fun isBluetoothConnect(): Boolean {
        return isWiredHeadsetOn() || isBluetoothAccess()
    }

    /**
     * 判断是否有蓝牙耳机接入
     */
    @SuppressLint("MissingPermission")
    fun isBluetoothAccess(): Boolean {

        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter() // 蓝牙耳机
        if (bluetoothAdapter == null) { // 若蓝牙耳机无连接
            return false
        } else if (bluetoothAdapter.isEnabled) {
            val a2dp =
                bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP) != BluetoothProfile.STATE_DISCONNECTED // 可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
            val headset =
                bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) != BluetoothProfile.STATE_DISCONNECTED // 蓝牙头戴式耳机，支持语音输入输出
            return a2dp || headset
        }
        return false
    }

    /**
     * 切换到外放
     */
    fun changeToSpeaker() {
        //注意此处，蓝牙未断开时使用MODE_IN_COMMUNICATION而不是MODE_NORMAL
        mAudioManager?.mode =
            if (isBluetoothAccess()) AudioManager.MODE_IN_COMMUNICATION else AudioManager.MODE_NORMAL
        mAudioManager?.stopBluetoothSco()
        mAudioManager?.isBluetoothScoOn = false
        mAudioManager?.isSpeakerphoneOn = true
    }


    /**
     * 切换到蓝牙音箱
     */
    fun changeToBluetooth() {
        mAudioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        mAudioManager?.startBluetoothSco()
        mAudioManager?.isBluetoothScoOn = true
        mAudioManager?.isSpeakerphoneOn = false
    }

    /**
     * 切换到耳机模式
     */
    fun changeToHeadset() {
        mAudioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        mAudioManager?.stopBluetoothSco()
        mAudioManager?.isBluetoothScoOn = false
        mAudioManager?.isSpeakerphoneOn = false
    }


    /**
     * 切换到听筒
     */
    fun changeToReceiver() {
        mAudioManager?.isSpeakerphoneOn = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mAudioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        } else {
            mAudioManager?.mode = AudioManager.MODE_IN_CALL
        }
    }

    /**
     * 注册广播
     */
    fun registerBluetoothReceiver() {
        mContext?.let {
            val intentFilter = IntentFilter()
            intentFilter.apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED) // 蓝牙状态改变
                addAction("android.bluetooth.BluetoothAdapter.STATE_OFF") // 本地蓝牙适配器已关闭
                addAction("android.bluetooth.BluetoothAdapter.STATE_ON") // 本地蓝牙适配器已打开，可以使用
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED) // 已和远程设备建立 ACL 连接
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED) // 与远程设备 ACL 断开连接
                priority = Int.MAX_VALUE
            }
            it.registerReceiver(bluetoothReceiver, intentFilter)
        }
    }

    /**
     * 注销广播
     */
    fun unregisterBluetoothReceiver() {
        mContext?.let {
            it.unregisterReceiver(bluetoothReceiver)
        }
    }

    private val bluetoothReceiver:BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            action ?: return
            //判断是否有有线耳机连接
            when (action) {
                Intent.ACTION_HEADSET_PLUG -> {
                    when (intent.getIntExtra("state", 0)) {
                        0 -> {         // 外放
                            Log.d(TAG, "onReceive: 耳机断开")
                            INSTANCE.changeToSpeaker()
                            INSTANCE.audioManageChangeListener?.invoke(
                                WiredHeadsetOnState,
                                false
                            )
                        }
                        1 -> {   // 耳机
                            Log.d(TAG, "onReceive: 耳机接入")
                            INSTANCE.changeToHeadset()
                            INSTANCE.audioManageChangeListener?.invoke(
                                WiredHeadsetOnState,
                                true
                            )
                        }
                        else -> {}
                    }
                }
                BluetoothDevice.ACTION_ACL_CONNECTED -> { // 蓝牙已连接
                    Log.d(TAG, "onReceive: 蓝牙已连接")
                    INSTANCE.changeToBluetooth()
                    INSTANCE.audioManageChangeListener?.invoke(bluetoothState, true)
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> { // 蓝牙已断开
                    Log.d(TAG, "onReceive: 蓝牙已断开")
                    INSTANCE.changeToSpeaker()
                    INSTANCE.audioManageChangeListener?.invoke(bluetoothState, false)
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                        BluetoothAdapter.STATE_OFF -> {
                            Log.d(TAG, "onReceive: 蓝牙关闭")
                        }
                        BluetoothAdapter.STATE_ON -> {
                            Log.d(TAG, "onReceive: 蓝牙开启")
                        }
                        else -> {
                            Log.d(TAG, " ACTION_STATE_CHANGED EXTRA_STATE: $state")
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "onReceive: action:$action")
                }
            }
        }
    }
}