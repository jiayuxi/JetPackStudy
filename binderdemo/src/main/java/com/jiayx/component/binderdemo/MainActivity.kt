package com.jiayx.component.binderdemo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.component.binderdemo.bean.Person
import com.jiayx.component.binderdemo.common.Stub
import com.jiayx.component.binderdemo.databinding.ActivityMainBinding
import com.jiayx.component.binderdemo.impl.ControllerStatusListenerImpl
import com.jiayx.component.binderdemo.impl.MyAidlInterfaceImpl
import com.jiayx.component.binderdemo.interfaces.IPersonManager
import com.jiayx.component.binderdemo.services.KtvService
import com.jiayx.component.binderdemo.services.RemoteService

class MainActivity : AppCompatActivity() {

    private var iPersonManager: IPersonManager? = null

    private var myAidlInterface: MyAidlInterface? = null

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        val intent = Intent(this, RemoteService::class.java)
//        intent.action = "com.jiayx.component.binderdemo"
//        bindService(intent, connection, BIND_AUTO_CREATE)

        val ktvIntent = Intent(this, KtvService::class.java)
        ktvIntent.action = "com.jiayx.component.binderdemo.KtvService"
        bindService(ktvIntent, ktvConnection, BIND_AUTO_CREATE)

        binding.binderOnClick.setOnClickListener {
            try {
                Log.e("jia_binder", "------------onClick:" + Thread.currentThread())
                iPersonManager?.addPerson(Person("jia_yx", 3))
                val persons: List<Person?>? = iPersonManager?.getPersonList()
                Log.e("jia_binder", persons.toString() + "," + Thread.currentThread())
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        binding.button.setOnClickListener {
            myAidlInterface?.pause("sorry!,pause ")
        }
        binding.button2.setOnClickListener {
            myAidlInterface?.play("hi , play")
        }
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e("jia_binder", "onServiceConnected: success")
            iPersonManager = Stub.asInterface(service) // proxy
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("jia_binder", "onServiceDisconnected: success")
            iPersonManager = null
        }
    }

    private val ktvConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myAidlInterface = MyAidlInterface.Stub.asInterface(service)
            Log.d("jia_binder", "ktv onServiceConnected: ")
            myAidlInterface?.setControllerStatusListener(object : ControllerStatusListenerImpl() {
                override fun onPauseSuccess() {
                    Log.d("jia_binder", "onPauseSuccess: ")
                }

                override fun onPauseFailed(errorCode: Int) {
                    Log.d("jia_binder", "onPauseFailed: $errorCode")
                }

                override fun onPlaySuccess() {
                    Log.d("jia_binder", "onPlaySuccess: ")
                }

                override fun onPlayFailed(errorCode: Int) {
                    Log.d("jia_binder", "onPlayFailed: $errorCode")
                }
            })
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("jia_binder", "ktv onServiceDisconnected: ")
            myAidlInterface = null
        }

    }
}