package com.jiayx.gesturedetector.tool

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.provider.Settings
import android.view.Window
import android.view.WindowManager


/**
 *Created by yuxi_
on 2022/5/21
 */
class BrightnessHelper(private val context: Context) {
    private var resolver: ContentResolver? = null
    private val maxBrightness = 255

    init {
        resolver = context.contentResolver
    }

    /*
     * 调整亮度范围
     */
    private fun adjustBrightnessNumber(brightness: Int): Int {
        var brightness = brightness
        if (brightness < 0) {
            brightness = 0
        } else if (brightness > 255) {
            brightness = 255
        }
        return brightness
    }

    /*
     * 关闭自动调节亮度
     */
    fun offAutoBrightness() {
        try {
            if (Settings.System.getInt(
                    resolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE
                ) === Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            ) {
                Settings.System.putInt(
                    resolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
                )
            }
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
    }

    /*
     * 获取系统亮度
     */
    fun getBrightness(): Int {
        return Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS, 255)
    }

    /*
     * 设置系统亮度，如果有设置了自动调节，请先调用offAutoBrightness()方法关闭自动调节，否则会设置失败
     */
    fun setSystemBrightness(newBrightness: Int) {
        Settings.System.putInt(
            resolver, Settings.System.SCREEN_BRIGHTNESS, adjustBrightnessNumber(newBrightness)
        )
    }

    fun getMaxBrightness(): Int {
        return maxBrightness
    }

    //设置当前APP的亮度
    fun setAppBrightness(brightnessPercent: Float, activity: Activity) {
        val window: Window = activity.window
        val layoutParams: WindowManager.LayoutParams = window.getAttributes()
        layoutParams.screenBrightness = brightnessPercent
        window.attributes = layoutParams
    }
}