package com.jiayx.wlan.wifi.listener;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * WIFI扫描结果的回调接口
 */
public interface OnWifiScanResultsListener {

    /**
     * 扫描结果的回调
     *
     * @param scanResults 扫描结果
     */
    void onScanResults(List<ScanResult> scanResults);
}
