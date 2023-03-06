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
import com.jiayx.component.binderdemo.interfaces.IPersonManager
import com.jiayx.component.binderdemo.services.RemoteService

class MainActivity : AppCompatActivity() {

    private var iPersonManager: IPersonManager? = null
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val intent = Intent(this, RemoteService::class.java)
        intent.action = "com.jiayx.component.binderdemo"
        bindService(intent, connection, BIND_AUTO_CREATE)
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
}