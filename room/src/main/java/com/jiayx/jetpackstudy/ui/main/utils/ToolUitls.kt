package com.jiayx.jetpackstudy.ui.main.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.text.ParsePosition
import java.text.SimpleDateFormat

/**
 *Created by yuxi_
on 2022/3/14
 */
/**
 * @desc  时间戳转字符串
 * @param Long timestamp
 * @example timestamp=1558322327000
 **/
fun transToString(time:Long):String{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time)
}
/**
 * 字符串转时间戳
 */
fun transToTimeStamp(date:String):Long{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date, ParsePosition(0)).time
}

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
fun Context.isConnectedNetwork(): Boolean = run {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    activeNetwork?.isConnectedOrConnecting == true
}