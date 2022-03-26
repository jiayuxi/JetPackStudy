package com.jiayx.wlan.wifi.listener;

/**
 * WIFI打开关闭的回调接口
 */
public interface OnWifiEnabledListener {

    /**
     * WIFI开关的回调
     * public static final int WIFI_STATE_DISABLED = 1;
     * public static final int WIFI_STATE_ENABLING = 2;
     * public static final int WIFI_STATE_ENABLED = 3;
     * public static final int WIFI_STATE_UNKNOWN = 4;
     */
    void onWifiEnabled(int state);
}
