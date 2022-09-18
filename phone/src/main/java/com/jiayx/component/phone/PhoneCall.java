package com.jiayx.component.phone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * author : Jia yu xi
 * date : 2022/9/18 13:04:04
 * description :
 */
class PhoneCall {
    public void rejectCall(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
                IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
                ITelephony telephony = ITelephony.Stub.asInterface(binder);
                telephony.endCall();
            } catch (NoSuchMethodException e) {
                Log.d("TAG", "", e);
            } catch (ClassNotFoundException e) {
                Log.d("TAG", "", e);
            } catch (Exception e) {

            }
        } else {
            TelephonyManager mTelMgr = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;
            try {
                Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
                getITelephonyMethod.setAccessible(true);
                ITelephony iTelephony = null;
                System.out.println("End call.");
                iTelephony = (ITelephony) getITelephonyMethod.invoke(mTelMgr, (Object[]) null);
                iTelephony.endCall();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Fail to answer ring call.");
            }
        }
    }

    /**
     * 4.1版本以上接听电话
     */
    private static final String MANUFACTURER_HTC = "HTC";
    public static void acceptCall_4_1(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ;
        //模拟无线耳机的按键来接听电话
        // for HTC devices we need to broadcast a connected headset
        boolean broadcastConnected = MANUFACTURER_HTC.equalsIgnoreCase(Build.MANUFACTURER) && !audioManager.isWiredHeadsetOn();
        if (broadcastConnected) {
            broadcastHeadsetConnected(context, false);
        }
        try {
            try {
                Runtime.getRuntime().exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
            } catch (IOException e) {
                // Runtime.exec(String) had an I/O problem, try to fall back
                String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
                Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                context.sendOrderedBroadcast(btnDown, enforcedPerm);
                context.sendOrderedBroadcast(btnUp, enforcedPerm);
            }
        } finally {
            if (broadcastConnected) {
                broadcastHeadsetConnected(context, false);
            }
        }
    }

    private static void broadcastHeadsetConnected(Context context, boolean connected) {
        Intent i = new Intent(Intent.ACTION_HEADSET_PLUG);
        i.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        i.putExtra("state", connected ? 1 : 0);
        i.putExtra("name", "mysms");
        try {
            context.sendOrderedBroadcast(i, null);
        } catch (Exception e) {
        }
    }
}
