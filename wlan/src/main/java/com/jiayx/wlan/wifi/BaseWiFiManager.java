package com.jiayx.wlan.wifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * WiFiManager基础类
 */
public class BaseWiFiManager {

    static WifiManager mWifiManager;

    private static final String TAG = "wifi_log";

    private static ConnectivityManager mConnectivityManager;

    BaseWiFiManager(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 添加开放网络配置
     *
     * @param ssid SSID
     * @return NetworkId
     */
    int setOpenNetwork(@NonNull String ssid) {
        if (TextUtils.isEmpty(ssid)) {
            return -1;
        }
        WifiConfiguration wifiConfiguration = getConfigFromConfiguredNetworksBySsid(ssid);
        if (null == wifiConfiguration) {
            // 生成配置
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = addDoubleQuotation(ssid);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfig.allowedAuthAlgorithms.clear();
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            // 添加配置并返回NetworkID
            return addNetwork(wifiConfig);
        } else {
            // 返回NetworkID
            return wifiConfiguration.networkId;
        }
    }

    /**
     * 添加WEP网络配置
     *
     * @param ssid     SSID
     * @param password 密码
     * @return NetworkId
     */
    int setWEPNetwork(@NonNull String ssid, @NonNull String password) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            return -1;
        }
        WifiConfiguration wifiConfiguration = getConfigFromConfiguredNetworksBySsid(ssid);
        if (null == wifiConfiguration) {
            // 添加配置
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = addDoubleQuotation(ssid);
            /**
             * special handling according to password length is a must for wep
             */
            int i = password.length();
            if (((i == 10 || (i == 26) || (i == 58))) && (password.matches("[0-9A-Fa-f]*"))) {
                wifiConfig.wepKeys[0] = password;
            } else {
                wifiConfig.wepKeys[0] = "\"" + password + "\"";
            }
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            // 添加配置并返回NetworkID
            return addNetwork(wifiConfig);
        } else {
            // 更新配置并返回NetworkID
            wifiConfiguration.wepKeys[0] = "\"" + password + "\"";
            return updateNetwork(wifiConfiguration);
        }
    }

    /**
     * 添加WPA网络配置
     *
     * @param ssid     SSID
     * @param password 密码
     * @return NetworkId
     */
    int setWPA2Network(@NonNull String ssid, @NonNull String password) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            return -1;
        }
        WifiConfiguration wifiConfiguration = getConfigFromConfiguredNetworksBySsid(ssid);
        if (null == wifiConfiguration) {
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = addDoubleQuotation(ssid);
            wifiConfig.preSharedKey = "\"" + password + "\"";
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfig.status = WifiConfiguration.Status.ENABLED;
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            // 添加配置并返回NetworkID
            return addNetwork(wifiConfig);
        } else {
            // 更新配置并返回NetworkID
            wifiConfiguration.preSharedKey = "\"" + password + "\"";
            return updateNetwork(wifiConfiguration);
        }
    }

    /**
     * 通过热点名获取热点配置
     *
     * @param ssid 热点名
     * @return 配置信息
     */
    public WifiConfiguration getConfigFromConfiguredNetworksBySsid(@NonNull String ssid) {
        ssid = addDoubleQuotation(ssid);
        List<WifiConfiguration> existingConfigs = getConfiguredNetworks();
        if (null != existingConfigs) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals(ssid)) {
                    return existingConfig;
                }
            }
        }
        return null;
    }


    /**
     * 获取WIFI的开关状态
     *
     * @return WIFI的可用状态
     */
    public boolean isWifiEnabled() {
        return null != mWifiManager && mWifiManager.isWifiEnabled();
    }

    /**
     * 判断WIFI是否连接
     *
     * @return 是否连接
     */
    public boolean isWifiConnected() {
        if (null != mConnectivityManager) {
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            return null != networkInfo && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    /**
     * 判断设备是否有网
     *
     * @return 网络状态
     */
    boolean hasNetwork() {
        if (null != mConnectivityManager) {
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前正在连接的WIFI信息
     *
     * @return 当前正在连接的WIFI信息
     */
    public WifiInfo getConnectionInfo() {
        if (null != mWifiManager) {
            return mWifiManager.getConnectionInfo();
        }
        return null;
    }

    /**
     * 获取当前正在连接的WIFI信息
     *
     * @return 当前正在连接的WIFI信息
     */
    public int getConnectionNetworkId() {
        if (null != mWifiManager) {
            return mWifiManager.getConnectionInfo().getNetworkId();
        }
        return 0;
    }

    /**
     * 扫描附近的WIFI
     */
    public void startScan() {
        if (null != mWifiManager) {
            try {
                mWifiManager.startScan();
            } catch (Exception e) {
                Log.d(TAG, "startScan: error: " + e.getMessage());
            }
        }
    }

    /**
     * 获取最近扫描的WIFI热点
     *
     * @return WIFI热点列表
     */
    public List<ScanResult> getScanResults() {
        // 得到扫描结果
        if (null != mWifiManager) {
            return mWifiManager.getScanResults();
        }
        return null;
    }

    /**
     * 获取配置过的WIFI信息
     *
     * @return 配置信息
     */
    @SuppressLint("MissingPermission")
    public List<WifiConfiguration> getConfiguredNetworks() {
        if (null != mWifiManager) {
            return mWifiManager.getConfiguredNetworks();
        }
        return null;
    }

    /**
     * 保持配置
     *
     * @return 保持是否成功
     */
    boolean saveConfiguration() {
        return null != mWifiManager && mWifiManager.saveConfiguration();
    }

    /**
     * 连接到网络
     *
     * @param networkId NetworkId
     * @return 连接结果
     */
    boolean enableNetwork(int networkId) {
        if (null != mWifiManager) {
            boolean isDisconnect = disconnectCurrentWifi();
            boolean isEnableNetwork = mWifiManager.enableNetwork(networkId, true);
            boolean isSave = mWifiManager.saveConfiguration();
            boolean isReconnect = mWifiManager.reconnect();
            return isDisconnect && isEnableNetwork && isSave && isReconnect;
        }
        return false;
    }

    /**
     * 连接到网络
     *
     * @param networkId NetworkId
     * @return 连接结果
     */
    boolean enableSaveNetwork(int networkId) {
        if (null != mWifiManager) {
            boolean isDisconnect = disconnectCurrentWifi();
            boolean isEnableNetwork = mWifiManager.enableNetwork(networkId, true);
            boolean isReconnect = mWifiManager.reconnect();
            return isDisconnect && isEnableNetwork && isReconnect;
        }
        return false;
    }

    /**
     * 添加网络配置
     *
     * @param wifiConfig 配置信息
     * @return NetworkId
     */
    int addNetwork(WifiConfiguration wifiConfig) {
        if (null != mWifiManager) {
            int networkId = mWifiManager.addNetwork(wifiConfig);
            if (-1 != networkId) {
                boolean isSave = mWifiManager.saveConfiguration();
                if (isSave) {
                    return networkId;
                }
            }
        }
        return -1;
    }

    /**
     * 更新网络配置
     *
     * @param wifiConfig 配置信息
     * @return NetworkId
     */
    private int updateNetwork(WifiConfiguration wifiConfig) {
        if (null != mWifiManager) {
            int networkId = mWifiManager.updateNetwork(wifiConfig);
            if (-1 != networkId) {
                boolean isSave = mWifiManager.saveConfiguration();
                if (isSave) {
                    return networkId;
                }
            }
        }
        return -1;
    }

    /**
     * 断开指定 WIFI
     *
     * @param netId netId
     * @return 是否断开
     */
    public boolean disconnectWifi(int netId) {
        if (null != mWifiManager) {
            boolean isDisable = mWifiManager.disableNetwork(netId);
            boolean isDisconnect = mWifiManager.disconnect();
            return isDisable && isDisconnect;
        }
        return false;
    }

    /**
     * 断开当前的WIFI
     *
     * @return 是否断开成功
     */
    public boolean disconnectCurrentWifi() {
        WifiInfo wifiInfo = getConnectionInfo();
        if (null != wifiInfo) {
            int networkId = wifiInfo.getNetworkId();
            return disconnectWifi(networkId);
        } else {
            // 断开状态
            return true;
        }
    }

    /**
     * 删除配置
     *
     * @param netId netId
     * @return 是否删除成功
     */
    public boolean deleteConfig(int netId) {
        if (null != mWifiManager) {
            if (getConnectionNetworkId() == netId) {
                mWifiManager.disableNetwork(netId);
            }
            boolean isRemove = mWifiManager.removeNetwork(netId);
            boolean isSave = mWifiManager.saveConfiguration();
            return (isRemove && isSave);
        }
        return false;
    }

    /**
     * 计算WIFI信号强度
     *
     * @param rssi WIFI信号
     * @return 强度
     */
    public int calculateSignalLevel(int rssi) {
        return WifiManager.calculateSignalLevel(rssi, 5);
    }

    /**
     * 获取WIFI的加密方式
     *
     * @param capabilities WIF的加密方式
     * @return 加密方式
     */
    public SecurityModeEnum getSecurityMode(@NonNull String capabilities) {
        //获取wifi加密类型
        if (capabilities.contains("WEP") || capabilities.contains("wep")) {
            return SecurityModeEnum.WEP;
        } else if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
            return SecurityModeEnum.WPA;
        } else if (capabilities.contains("WPA2") || capabilities.contains("wpa2")) {
            return SecurityModeEnum.WPA2;
        } else {
            // 没有加密
            return SecurityModeEnum.OPEN;
        }
    }

    /**
     * 添加双引号
     *
     * @param text 待处理字符串
     * @return 处理后字符串
     */
    public String addDoubleQuotation(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return "\"" + text + "\"";
    }

    /**
     * 接着通过isExsit（String SSID）方法判断系统是否保存着当前WiFi的信息。
     *
     * @param SSID
     * @return
     */
    @SuppressLint("MissingPermission")
    public WifiConfiguration isExsit(String SSID) {
        if (mWifiManager != null) {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }
}
