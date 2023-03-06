package com.jiayx.component.binderdemo.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.jiayx.component.binderdemo.IControllerStatusListener
import com.jiayx.component.binderdemo.impl.ControllerStatusListenerImpl
import com.jiayx.component.binderdemo.impl.MyAidlInterfaceImpl

/**
 *  author : Jia yu xi
 *  date : 2023/3/6 22:54:54
 *  description :
 */
class KtvService : Service() {

    companion object {
        private const val TAG = "jia_binder"
    }

    private var listener: IControllerStatusListener? = null
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ktvService 初始化")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return KtvBinder()
    }

    inner class KtvBinder : MyAidlInterfaceImpl() {

        override fun pause(pause: String?) {
            super.pause(pause)
            Log.d(TAG, "pause:${pause} ")
            listener?.let {
                Thread {
                    try {
                        Thread.sleep(1000)
                        if (System.currentTimeMillis() % 2 == 0L) {
                            it.onPauseSuccess()
                        } else {
                            it.onPauseFailed(1002)
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (remoteException: RemoteException) {
                        remoteException.printStackTrace()
                    }
                }.start()
            }
        }

        override fun play(paly: String?) {
            super.play(paly)
            Log.d(TAG, "play:${paly} ")
            listener?.let {
                Thread {
                    try {
                        Thread.sleep(1000)
                        if (System.currentTimeMillis() % 2 == 0L) {
                            it.onPlaySuccess()
                        } else {
                            it.onPlayFailed(1002)
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (remoteException: RemoteException) {
                        remoteException.printStackTrace()
                    }
                }.start()
            }
        }

        override fun setControllerStatusListener(i: IControllerStatusListener?) {
            super.setControllerStatusListener(i)
            listener = i
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: ")
    }

}