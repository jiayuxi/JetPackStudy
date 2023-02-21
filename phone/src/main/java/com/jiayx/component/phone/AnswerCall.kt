package com.jiayx.component.phone

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import com.android.internal.telephony.ITelephony
import java.io.IOException
import java.lang.reflect.Method

/**
 *  author : Jia yu xi
 *  date : 2022/9/18 13:18:18
 *  description :
 */
object AnswerCall {
    @SuppressLint("MissingPermission")
    private fun endCallAction(context: Context) {
        try {
            /*if (Build.VERSION.SDK_INT >= 28) {
                val tcm = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                tcm.endCall()
            } else {
                try {
                    val loadClass: Class<*> =
                        javaClass.classLoader.loadClass("android.os.ServiceManager")
                    val method: Method =
                        loadClass.getDeclaredMethod("getService", String::class.java)
                    //这里也可以直接用 context.getSystemService(Context.TELEPHONY_SERVICE ) as TelephonyManager
                    //注意 TELECOM* 和 TELEPHONY* 区别
                    val invoke: IBinder = method.invoke(null, Context.TELEPHONY_SERVICE) as IBinder
                    val iTelephony: ITelephony = ITelephony.Stub.asInterface(invoke)
                    iTelephony.endCall()
                } catch (e: NoSuchMethodException) {
                    Log.d("TAG", "", e)
                } catch (e: ClassNotFoundException) {
                    Log.d("TAG", "", e)
                } catch (e: java.lang.Exception) {
                }
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
   
    /**
     * 4.1版本以上接听电话
     */
    private const val MANUFACTURER_HTC = "HTC"
    fun acceptCall_4_1(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        //模拟无线耳机的按键来接听电话
        // for HTC devices we need to broadcast a connected headset
        val broadcastConnected = MANUFACTURER_HTC.equals(
            Build.MANUFACTURER,
            ignoreCase = true
        ) && !audioManager.isWiredHeadsetOn
        if (broadcastConnected) {
            broadcastHeadsetConnected(context, false)
        }
        try {
            try {
                Runtime.getRuntime()
                    .exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK))
            } catch (e: IOException) {
                // Runtime.exec(String) had an I/O problem, try to fall back
                val enforcedPerm = "android.permission.CALL_PRIVILEGED"
                val btnDown = Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                    Intent.EXTRA_KEY_EVENT, KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK
                    )
                )
                val btnUp = Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                    Intent.EXTRA_KEY_EVENT, KeyEvent(
                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK
                    )
                )
                context.sendOrderedBroadcast(btnDown, enforcedPerm)
                context.sendOrderedBroadcast(btnUp, enforcedPerm)
            }
        } finally {
            if (broadcastConnected) {
                broadcastHeadsetConnected(context, false)
            }
        }
    }

    private fun broadcastHeadsetConnected(context: Context, connected: Boolean) {
        val i = Intent(Intent.ACTION_HEADSET_PLUG)
        i.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY)
        i.putExtra("state", if (connected) 1 else 0)
        i.putExtra("name", "mysms")
        try {
            context.sendOrderedBroadcast(i, null)
        } catch (e: java.lang.Exception) {
        }
    }
}