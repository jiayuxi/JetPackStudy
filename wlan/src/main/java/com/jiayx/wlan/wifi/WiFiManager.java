package com.jiayx.wlan.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jiayx.wlan.wifi.listener.OnWifiConnectListener;
import com.jiayx.wlan.wifi.listener.OnWifiEnabledListener;
import com.jiayx.wlan.wifi.listener.OnWifiScanResultsListener;
import com.jiayx.wlan.adapter.WifiListAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * WifiManager : https://developer.android.com/reference/android/net/wifi/WifiManager.html
 * WifiConfiguration : https://developer.android.com/reference/android/net/wifi/WifiConfiguration.html
 * ScanResult : https://developer.android.com/reference/android/net/wifi/ScanResult.html
 * WifiInfo : https://developer.android.com/reference/android/net/wifi/WifiInfo.html
 * Wifi管理
 */
public class WiFiManager extends BaseWiFiManager {

    private static final String TAG = "WiFiManager";
    private static WiFiManager mWiFiManager;
    private static CallBackHandler mCallBackHandler = new CallBackHandler();
    // WIFI 开关状态回调
    private static final int WIFI_STATE_DISABLING = 0;
    private static final int WIFI_STATE_DISABLED = 1;
    private static final int WIFI_STATE_ENABLING = 2;
    private static final int WIFI_STATE_ENABLED = 3;
    private static final int WIFI_STATE_UNKNOWN = 4;
    //
    private static final int SCAN_RESULTS_UPDATED = 5;
    private static final int WIFI_CONNECT_LOG = 6;
    private static final int WIFI_CONNECT_SUCCESS = 7;
    private static final int WIFI_CONNECT_FAILURE = 8;
    private static final int WIFI_DISCONNECT = 9;
    private static final String DEFAULT_AP_PASSWORD = "12345678";

    private static boolean isWifiConnecting = false;

    private WiFiManager(Context context) {
        super(context);
    }

    public static WiFiManager getInstance(Context context) {
        if (null == mWiFiManager) {
            synchronized (WiFiManager.class) {
                if (null == mWiFiManager) {
                    mWiFiManager = new WiFiManager(context);
                }
            }
        }
        return mWiFiManager;
    }

    /**
     * 打开Wifi
     */
    public void openWiFi() {
        if (!isWifiEnabled() && null != mWifiManager) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭Wifi
     */
    public void closeWiFi() {
        if (isWifiEnabled() && null != mWifiManager) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 创建热点，这里只能是7.0或7.0之前的版本
     * 手动设置热点名和密码
     */
    private void createWifiHotspot7() {

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "123456789";
        config.preSharedKey = "123456789";
        config.hiddenSSID = true;
        config.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN);//开放系统认证
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        //设置加密的方式，这个对应小米手机MIUI
        int indexOfWPA2_PSK = 4;
        //从WifiConfiguration.KeyMgmt数组中查找WPA2_PSK的值
        for (int i = 0; i < WifiConfiguration.KeyMgmt.strings.length; i++) {
            if (WifiConfiguration.KeyMgmt.strings[i].equals("WPA2_PSK")) {
                indexOfWPA2_PSK = i;
                break;
            }
        }
        config.allowedKeyManagement.set(indexOfWPA2_PSK);

        config.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.CCMP);
        config.status = WifiConfiguration.Status.ENABLED;

        //通过反射调用设置热点
        try {
            Method method = mWifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            boolean enable = (Boolean) method.invoke(mWifiManager, config, true);
            if (enable) {
                Log.i(TAG, "热点已经开启##################################");
            } else {
                Log.i(TAG, "热点开启失败##################################");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 8.0 开启热点方法
     * 这里使用WPA2加密
     *
     * @param ssid
     * @param password
     */
    private void createWifiHotspot8(String ssid, String password) {

        WifiConfiguration wifiConfig = new WifiConfiguration();
        Log.i(TAG, "热点准备开启##################################");

        // 清理配置
        wifiConfig.SSID = new String(ssid);
        wifiConfig.allowedAuthAlgorithms.clear();
        wifiConfig.allowedGroupCiphers.clear();
        wifiConfig.allowedKeyManagement.clear();
        wifiConfig.allowedPairwiseCiphers.clear();
        wifiConfig.allowedProtocols.clear();

        // 如果密码为空或者小于8位数，则使用默认的密码
        if (null != password && password.length() >= 8) {
            wifiConfig.preSharedKey = password;
        } else {
            wifiConfig.preSharedKey = DEFAULT_AP_PASSWORD;
        }
        wifiConfig.hiddenSSID = false;
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfig.allowedKeyManagement.set(4);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfig.status = WifiConfiguration.Status.ENABLED;

    }

    /**
     * 关闭热点
     */
    private void closeWifiHotspot8() {
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(mWifiManager, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 连接到开放网络
     *
     * @param ssid 热点名
     * @return 配置是否成功
     */
    public boolean connectOpenNetwork(@NonNull String ssid) {
        // 获取networkId
        int networkId = setOpenNetwork(ssid);
        if (-1 != networkId) {
            // 保存配置
            boolean isSave = saveConfiguration();
            // 连接网络
            boolean isEnable = enableNetwork(networkId);

            return isSave && isEnable;
        }
        return false;

    }

    /**
     * 连接到WEP网络
     *
     * @param ssid     热点名
     * @param password 密码
     * @return 配置是否成功
     */
    public boolean connectWEPNetwork(@NonNull String ssid, @NonNull String password) {
        // 获取networkId
        int networkId = setWEPNetwork(ssid, password);
        if (-1 != networkId) {
            // 保存配置
            boolean isSave = saveConfiguration();
            // 连接网络
            boolean isEnable = enableNetwork(networkId);

            return isSave && isEnable;
        }
        return false;
    }

    /**
     * 连接到WPA2网络
     *
     * @param ssid     热点名
     * @param password 密码
     * @return 配置是否成功
     */
    public boolean connectWPA2Network(@NonNull String ssid, @NonNull String password) {
        // 获取networkId
        int networkId = setWPA2Network(ssid, password);
        if (-1 != networkId) {
            // 保存配置
            boolean isSave = saveConfiguration();
            // 连接网络
            boolean isEnable = enableNetwork(networkId);

            return isSave && isEnable;
        }
        return false;
    }

    /**
     * 接着通过isExsits（String SSID）方法判断系统是否保存着当前WiFi的信息。
     */
    public WifiConfiguration isWifiConfigExits(String SSID) {
        return isExsit(SSID);
    }

    /**
     * 获取到所要连接WiFi的配置信息,则直接调用 connectNetwork(WifiConfiguration config) 进行连接
     *
     * @param wifiConfiguration
     * @return
     */
    public boolean connectNetwork(WifiConfiguration wifiConfiguration) {
//        disconnectCurrentWifi();
        if (wifiConfiguration != null) {
            boolean isEnable = enableSaveNetwork(wifiConfiguration.networkId);
            return isEnable;
        }
        return false;
    }

    public String getWifiConnectSSID() {
        WifiInfo wifiInfo = getConnectionInfo();
        if (wifiInfo != null) {
            return wifiInfo.getSSID();
        }
        return "";
    }
    /* *******************************************************************************************/

    /**
     * 广播接收者
     */
    public static class NetworkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获得WifiManager的实例对象
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            switch (intent.getAction()) {
                //WIFI状态发生变化的通知
                case WifiManager.WIFI_STATE_CHANGED_ACTION:// WiFi 开关状态的广播监听
                    switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)) {
                        case WifiManager.WIFI_STATE_ENABLING:
                            Log.d(TAG, "onReceive: 正在打开 WIFI...");
                            mCallBackHandler.sendEmptyMessage(WIFI_STATE_ENABLING);
                            break;
                        case WifiManager.WIFI_STATE_ENABLED:
                            Log.d(TAG, "onReceive: WIFI 已打开");
                            mCallBackHandler.sendEmptyMessage(WIFI_STATE_ENABLED);
                            break;
                        case WifiManager.WIFI_STATE_DISABLING:
                            Log.d(TAG, "onReceive: 正在关闭 WIFI...");
                            mCallBackHandler.sendEmptyMessage(WIFI_STATE_DISABLING);
                            break;
                        case WifiManager.WIFI_STATE_DISABLED:
                            Log.d(TAG, "onReceive: WIFI 已关闭");
                            mCallBackHandler.sendEmptyMessage(WIFI_STATE_DISABLED);
                            break;
                        case WifiManager.WIFI_STATE_UNKNOWN:
                        default:
                            Log.d(TAG, "onReceive: WIFI 状态未知!");
                            mCallBackHandler.sendEmptyMessage(WIFI_STATE_UNKNOWN);
                            break;
                    }
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION: // WIFI扫描完成
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean isUpdated = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                        Log.d(TAG, "onReceive: WIFI扫描  " + (isUpdated ? "完成" : "未完成"));
                    } else {
                        Log.d(TAG, "onReceive: WIFI扫描完成");
                    }
                    if (!isWifiConnecting) {
                        sendScanResults(wifiManager);
                    }
                    break;
                case WifiManager.NETWORK_STATE_CHANGED_ACTION: //WIFI网络状态变化通知
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (null != networkInfo && networkInfo.getType() == 1) {
                        Log.d(TAG, "onReceive: state: " + networkInfo.getState() + " , detailedState : " + networkInfo.getDetailedState());
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTING
                                && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTING) {
                            Log.d(TAG, "onReceive: 正在连接");
                            isWifiConnecting = true;
                            sendConnectState(WIFI_CONNECT_LOG, "正在连接...");
                        } else if (networkInfo.getState() == NetworkInfo.State.CONNECTING
                                && networkInfo.getDetailedState() == NetworkInfo.DetailedState.AUTHENTICATING) {
                            Log.d(TAG, "onReceive: 正在身份验证");
                            sendConnectState(WIFI_CONNECT_LOG, "正在身份验证...");
                        } else if (networkInfo.getState() == NetworkInfo.State.CONNECTING
                                && networkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                            Log.d(TAG, "onReceive: 正在获取IP");
                            sendConnectState(WIFI_CONNECT_LOG, "正在获取IP...");
                        } else if (networkInfo.getState() == NetworkInfo.State.CONNECTED
                                && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            if (wifiInfo != null) {
                                isWifiConnecting = false;
                                String ssid = wifiInfo.getSSID();
                                sendConnectState(WIFI_CONNECT_SUCCESS, ssid);
                                Log.d(TAG, "onReceive: 网络连接成功 ssid = " + ssid);
                            }
                        } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED
                                && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                            Log.d(TAG, "onReceive: 网络连接断开 ");
                            sendConnectState(WIFI_DISCONNECT, null);
                        }
                    }
                    break;

                //WIFI连接状态变化的时候
                case WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION:
                    boolean isConnected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
                    Log.d(TAG, "onReceive: SUPPLICANT_CONNECTION_CHANGE_ACTION  isConnected = " + isConnected);
                    break;
                case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION: //wifi连接结果通知   WIFI连接请求状态发生改变
                    // 获取连接状态
                    SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                    Log.i(TAG, "onReceive: supplicantState :  " + supplicantState);
                    switch (supplicantState) {
                        case INTERFACE_DISABLED: // 接口禁用
                            break;
                        case DISCONNECTED:// 断开连接
                            int disConnectError = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                            Log.d(TAG, "onReceive: disConnectError  : " + disConnectError);
                            if (disConnectError == WifiManager.ERROR_AUTHENTICATING) {//这里可以获取到监听连接wifi密码错误的时候进行回调
                                sendConnectState(WIFI_CONNECT_FAILURE, "密码可能错误");
                                isWifiConnecting = false;
                                Log.d(TAG, "onReceive: WiFi 密码错误");
                            } else if (disConnectError == 123 && isWifiConnecting) {
                                isWifiConnecting = false;
                                sendConnectState(WIFI_CONNECT_FAILURE, "身份验证有问题");
                            }
                            break;
                        case INACTIVE: // 不活跃的
                            /* WifiInfo connectFailureInfo = wifiManager.getConnectionInfo();
                           if (null != connectFailureInfo) {
                                // 断开连接
                                int networkId = connectFailureInfo.getNetworkId();
                                boolean isDisable = wifiManager.disableNetwork(networkId);
                                boolean isDisconnect = wifiManager.disconnect();
                                if (isDisable && isDisconnect) {
                                    Message wifiConnectFailureMessage = Message.obtain();
                                    wifiConnectFailureMessage.what = WIFI_CONNECT_FAILURE;
                                    wifiConnectFailureMessage.obj = "断开连接";
                                    mCallBackHandler.sendMessage(wifiConnectFailureMessage);
                                }*/
                            Log.i(TAG, "onReceive: 不活跃的 ");
                            int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                            if (error == WifiManager.ERROR_AUTHENTICATING && isWifiConnecting) {//这里可以获取到监听连接wifi密码错误的时候进行回调
                                isWifiConnecting = false;
                                sendConnectState(WIFI_CONNECT_FAILURE, "密码可能错误");
                            }
                            break;
                        case SCANNING: // 正在扫描
                            Log.i(TAG, "onReceive: SCANNING 正在扫描");
                            break;
                        case AUTHENTICATING: // 正在验证
                            break;
                        case ASSOCIATING: // 正在关联
                            break;
                        case ASSOCIATED: // 已经关联
                            break;
                        case FOUR_WAY_HANDSHAKE: // 四次握手
                            break;
                        case GROUP_HANDSHAKE:  // 组握手
                            break;
                        case COMPLETED: // 完成
                            break;
                        case DORMANT: // 休眠
                            break;
                        case UNINITIALIZED: // 未初始化
                            break;
                        case INVALID: // 无效的
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private static class CallBackHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WIFI_STATE_ENABLING:
                    if (null != mOnWifiEnabledListener) {
                        mOnWifiEnabledListener.onWifiEnabled(WIFI_STATE_ENABLING);
                    }
                    break;
                case WIFI_STATE_ENABLED: // WIFI已经打开
                    if (null != mOnWifiEnabledListener) {
                        mOnWifiEnabledListener.onWifiEnabled(WIFI_STATE_ENABLED);
                    }
                    break;
                case WIFI_STATE_DISABLING:
                    if (null != mOnWifiEnabledListener) {
                        mOnWifiEnabledListener.onWifiEnabled(WIFI_STATE_DISABLING);
                    }
                    break;
                case WIFI_STATE_DISABLED: // WIFI已经关闭
                    if (null != mOnWifiEnabledListener) {
                        mOnWifiEnabledListener.onWifiEnabled(WIFI_STATE_DISABLED);
                    }
                    break;
                case WIFI_STATE_UNKNOWN:
                    if (null != mOnWifiEnabledListener) {
                        mOnWifiEnabledListener.onWifiEnabled(WIFI_STATE_UNKNOWN);
                    }
                    break;
                case SCAN_RESULTS_UPDATED: // WIFI扫描完成
                    if (null != mOnWifiScanResultsListener) {
                        @SuppressWarnings("unchecked")
                        List<ScanResult> scanResults = (List<ScanResult>) msg.obj;
                        mOnWifiScanResultsListener.onScanResults(scanResults);
                    }
                    break;
                case WIFI_CONNECT_LOG: // WIFI连接的信息
                    if (null != mOnWifiConnectListener) {
                        String log = (String) msg.obj;
                        mOnWifiConnectListener.onWiFiConnectLog(log);
                    }
                    break;
                case WIFI_CONNECT_SUCCESS: // WIFI连接完成
                    if (null != mOnWifiConnectListener) {
                        String ssid = (String) msg.obj;
                        mOnWifiConnectListener.onWiFiConnectSuccess(ssid);
                    }
                    break;
                case WIFI_CONNECT_FAILURE: // WIFI连接失败
                    if (null != mOnWifiConnectListener) {
                        String ssid = (String) msg.obj;
                        mOnWifiConnectListener.onWiFiConnectFailure(ssid);
                    }
                    break;
                case WIFI_DISCONNECT:// WiFi 断开连接
                    if (null != mOnWifiConnectListener) {
                        mOnWifiConnectListener.onWifiDisconnected();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static OnWifiEnabledListener mOnWifiEnabledListener;

    private static OnWifiScanResultsListener mOnWifiScanResultsListener;

    private static OnWifiConnectListener mOnWifiConnectListener;

    public void setOnWifiEnabledListener(OnWifiEnabledListener listener) {
        mOnWifiEnabledListener = listener;
    }

    public void removeOnWifiEnabledListener() {
        mOnWifiEnabledListener = null;
    }

    public void setOnWifiScanResultsListener(OnWifiScanResultsListener listener) {
        mOnWifiScanResultsListener = listener;
    }

    public void removeOnWifiScanResultsListener() {
        mOnWifiScanResultsListener = null;
    }

    public void setOnWifiConnectListener(OnWifiConnectListener listener) {
        mOnWifiConnectListener = listener;
    }

    public void removeOnWifiConnectListener() {
        mOnWifiConnectListener = null;
    }

    /**
     * 发送 WIFI 扫描的结果
     */
    private static void sendScanResults(WifiManager wifiManager) {
        Message scanResultsMessage = Message.obtain();
        scanResultsMessage.what = SCAN_RESULTS_UPDATED;
        scanResultsMessage.obj = wifiManager.getScanResults();
        mCallBackHandler.sendMessage(scanResultsMessage);
    }

    /**
     * 发送 WiFi 连接的状态
     */
    private static void sendConnectState(int state, String message) {
        Message msg = Message.obtain();
        msg.what = state;
        msg.obj = message;
        mCallBackHandler.sendMessage(msg);
    }

    /**
     * 排除重复
     *
     * @param scanResults 带处理的数据
     * @return 去重数据
     */
    public ArrayList<WifiListAdapter.Companion.WifiItem> excludeRepetition(List<ScanResult> scanResults) {

        HashMap<String, ScanResult> hashMap = new HashMap<>();
        for (ScanResult scanResult : scanResults) {
            String ssid = scanResult.SSID;
            if (TextUtils.isEmpty(ssid)) {
                continue;
            }
            ScanResult tempResult = hashMap.get(ssid);
            if (null == tempResult) {
                hashMap.put(ssid, scanResult);
                continue;
            }
            if (WifiManager.calculateSignalLevel(tempResult.level, 100) < WifiManager.calculateSignalLevel(scanResult.level, 100)) {
                hashMap.put(ssid, scanResult);
            }
        }
        ArrayList<WifiListAdapter.Companion.WifiItem> results = new ArrayList<>();
        for (Map.Entry<String, ScanResult> entry : hashMap.entrySet()) {
            ScanResult entryValue = entry.getValue();
            results.add(new WifiListAdapter.Companion.WifiItem(entryValue.SSID, entryValue.level
                    , entryValue.BSSID, entryValue.capabilities, entryValue.frequency, false, null, 0));
        }
        List<WifiConfiguration> configurations = getConfiguredNetworks();
        if (configurations != null) {
            configurations.stream().forEach(item -> {
                results.stream().forEach(result -> {
                    if (item.SSID.equals(addDoubleQuotation(result.getSsid()))) {
                        result.setSave(true);
                        result.setNetworkState("已保存");
                        result.setNumber(1);
                    }
                });
            });
            WifiInfo wifiInfo = getConnectionInfo();
            if (wifiInfo != null) {
                Log.d(TAG, "onReceive: excludeRepetition: 连接的WiFi 信息 ： " + wifiInfo.getSSID());
                results.stream().forEach(wifiItem -> {
                    if (wifiInfo.getSSID().equals(addDoubleQuotation(wifiItem.getSsid()))) {
                        wifiItem.setNetworkState("已连接");
                        wifiItem.setNumber(2);
                    }
                });
            }
        }
        return results;
    }

}
