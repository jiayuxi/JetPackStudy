package com.jiayx.wlan.viewModel

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jiayx.wlan.wifi.SecurityModeEnum
import com.jiayx.wlan.wifi.WiFiManager
import com.jiayx.wlan.wifi.listener.OnWifiConnectListener
import com.jiayx.wlan.wifi.listener.OnWifiEnabledListener
import com.jiayx.wlan.wifi.listener.OnWifiScanResultsListener
import com.jiayx.wlan.adapter.WifiListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

open class WifiViewModel(application: Application) : AndroidViewModel(application),
    OnWifiScanResultsListener, OnWifiConnectListener,
    OnWifiEnabledListener {

    open var scanResultsListener: ((List<WifiListAdapter.Companion.WifiItem>?) -> Unit)? = null
    open var wifiEnabledListener: ((state: Int) -> Unit)? = null
    open var wifiConnectSuccessListener: ((string: String?) -> Unit)? = null
    open var wifiConnectFailureListener: ((string: String?) -> Unit)? = null
    open var wifiConnectStateListener: ((string: String?) -> Unit)? = null
    open var wifiDisconnectedListener: (() -> Unit)? = null

    private val networkChangeReceiver: WiFiManager.NetworkBroadcastReceiver by lazy {
        WiFiManager.NetworkBroadcastReceiver()
    }

    private val mWiFiManager: WiFiManager by lazy {
        WiFiManager.getInstance(application)
    }
    var currentSSID: String? = null
    var encryptionType: SecurityModeEnum? = null

    /**
     * 添加监听
     */

    fun addInterfaceListener() {
        // 添加监听
        mWiFiManager.setOnWifiEnabledListener(this)
        mWiFiManager.setOnWifiScanResultsListener(this)
        mWiFiManager.setOnWifiConnectListener(this)
    }

    /**
     * 移除监听
     */
    fun removeInterfaceListener() {
        // 移除监听
        mWiFiManager.removeOnWifiEnabledListener()
        mWiFiManager.removeOnWifiScanResultsListener()
        mWiFiManager.removeOnWifiConnectListener()
    }

    /**
     * 注册监听的广播
     */
    fun initReceiver(context: Context?) {
        context?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS") //添加监听网络改变的动作
            intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED") //添加监听网络改变的动作
            intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE") //添加监听网络改变的动作
            intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE") //添加监听网络改变的动作
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            it.registerReceiver(networkChangeReceiver, intentFilter) //注册广播
        }
    }

    /**
     * 注销监听的广播
     */
    fun unregisterReceiver(context: Context?) {
        context?.unregisterReceiver(networkChangeReceiver)
    }

    /**
     * 开启WiFi
     */
    fun enabledWifi() {
        mWiFiManager.openWiFi()
    }

    /**
     * 关闭WiFi
     */
    fun closeWifi() {
        mWiFiManager.closeWiFi()
    }

    /**
     * 刷新 WiFi 列表
     */
    fun startScanWifi() {
        mWiFiManager.startScan()
    }

    /**
     * 获取 wifi 列表
     */
    fun getScanResults() {
        return excludeRepetition(mWiFiManager.scanResults)
    }

    /**
     * 获取连接的wifi 信息
     */
    fun getConnectionInfo(): WifiInfo? {
        return mWiFiManager.connectionInfo
    }

    /**
     * 根据 ssid 获取配置过的网络信息
     */

    fun getConfiguredNetworksBySsid(ssid: String): WifiConfiguration? {
        return mWiFiManager.getConfigFromConfiguredNetworksBySsid(ssid)
    }

    /**
     * 添加双引号
     *
     * @param text 待处理字符串
     * @return 处理后字符串
     */
    fun addDoubleQuotation(text: String): String {
        return mWiFiManager.addDoubleQuotation(text)
    }

    /**
     * 断开指定 WIFI
     *
     * @param networkID netId
     * @return 是否断开
     */
    fun disconnectWifi(networkID: Int): Boolean {
        return mWiFiManager.disconnectWifi(networkID)
    }

    /**
     *
     * @return 断开连接的 WIFI
     */
    fun disconnectWifi(): Boolean {
        return mWiFiManager.disconnectCurrentWifi()
    }

    /**
     * 删除配置
     *
     * @param networkID netId
     * @return 是否删除成功
     */
    fun deleteNetworkConfig(networkID: Int): Boolean {
        return mWiFiManager.deleteConfig(networkID)
    }

    /**
     * 连接到WEP网络
     *
     * @param ssid     热点名
     * @param password 密码
     * @return 配置是否成功
     */
    fun connectWEPNetwork(ssid: String, password: String): Boolean {
        return mWiFiManager.connectWEPNetwork(ssid, password)
    }

    /**
     * 连接到WPA2网络
     *
     * @param ssid     热点名
     * @param password 密码
     * @return 配置是否成功
     */
    fun connectWPA2Network(ssid: String, password: String): Boolean {
        return mWiFiManager.connectWPA2Network(ssid, password)
    }

    /**
     * 连接到开放网络
     *
     * @param ssid 热点名
     * @return 配置是否成功
     */
    fun connectOpenNetwork(ssid: String): Boolean {
        return mWiFiManager.connectOpenNetwork(ssid)
    }

    /**
     * 获取到所要连接WiFi的配置信息,则直接调用 connectNetwork(WifiConfiguration config) 进行连接
     *
     * @param wifiConfiguration
     * @return
     */
    fun connectNetwork(wifiConfiguration: WifiConfiguration): Boolean {
        return mWiFiManager.connectNetwork(wifiConfiguration)
    }

    /**
     * 接着通过isExsit（String SSID）方法判断系统是否保存着当前WiFi的信息。
     *
     * @param SSID
     * @return
     */
    fun isWifiConfigExits(SSID: String): WifiConfiguration? {
        return mWiFiManager.isWifiConfigExits(SSID)
    }

    /**
     * 获取当前连接的 WiFi SSID
     */
    fun getWifiConnectSSID(): String {
        return if ("<unknown ssid>" == mWiFiManager.wifiConnectSSID) "" else mWiFiManager.wifiConnectSSID
    }

    /**
     * 获取WIFI的开关状态
     *
     * @return WIFI的可用状态
     */
    fun isWifiEnabled(): Boolean {
        return mWiFiManager.isWifiEnabled
    }

    /**
     * 获取WIFI的加密方式
     *
     * @param capabilities WIF的加密方式
     * @return 加密方式
     */
    fun securityMode(capabilities: String): SecurityModeEnum {
        return mWiFiManager.getSecurityMode(capabilities)
    }

    fun isCheckSSIDConnect(ssid: String): Boolean {

        return addDoubleQuotation(ssid) == mWiFiManager.wifiConnectSSID
    }
    /* ---------------------------------------------------------------------------------*/
    /**
     * WIFI列表刷新后的回调
     *
     * @param scanResults 扫描结果
     */
    override fun onScanResults(scanResults: MutableList<ScanResult>?) {
        excludeRepetition(scanResults)
    }

    /**
     * WIFI连接的Log得回调
     *
     * @param log log
     */
    override fun onWiFiConnectLog(log: String?) {
        wifiConnectStateListener?.invoke(log)
    }

    /**
     * WIFI连接成功的回调
     *
     * @param SSID 热点名
     */
    override fun onWiFiConnectSuccess(SSID: String?) {
        Log.d("", "onWiFiConnectSuccess: ")
        wifiConnectSuccessListener?.invoke(SSID)
    }

    /**
     * WIFI连接失败的回调
     *
     * @param error 热点名
     */
    override fun onWiFiConnectFailure(error: String?) {
        wifiConnectFailureListener?.invoke(error)
    }

    /**
     * 断开连接
     */
    override fun onWifiDisconnected() {
        wifiDisconnectedListener?.invoke()
    }

    override fun onWifiEnabled(state: Int) {
        wifiEnabledListener?.invoke(state)
    }

    private fun excludeRepetition(scanResults: List<ScanResult>?) {

        scanResults?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val excludeRepetition = mWiFiManager.excludeRepetition(scanResults)
                val c1: Comparator<WifiListAdapter.Companion.WifiItem> = Comparator { o1, o2 ->
                    if (o2.number == o1.number) {
                        o2.level - o1.level
                    } else {
                        o2.number - o1.number
                    }
                }
                excludeRepetition.sortWith(c1)

                scanResultsListener?.invoke(excludeRepetition)
            }
        } ?: kotlin.run {
            scanResultsListener?.invoke(null)
        }
    }

    fun updateWlanState(list: ArrayList<WifiListAdapter.Companion.WifiItem>, str: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val arrayList = arrayListOf<WifiListAdapter.Companion.WifiItem>()
            arrayList.addAll(list)
            arrayList.forEach { wifiItem ->
                if (currentSSID == wifiItem.ssid) {
                    wifiItem.networkState = str
                    wifiItem.number = 2
                    wifiItem.isSave = true
                } else {
                    if (wifiItem.isSave) {
                        wifiItem.networkState = "已保存"
                        wifiItem.number = 1
                    }
                }
            }
            val c1: Comparator<WifiListAdapter.Companion.WifiItem> = Comparator { o1, o2 ->
                if (o2.number == o1.number) {
                    o2.level - o1.level
                } else {
                    o2.number - o1.number
                }
            }
            arrayList.sortWith(c1)
            scanResultsListener?.invoke(arrayList)
        }
    }
}