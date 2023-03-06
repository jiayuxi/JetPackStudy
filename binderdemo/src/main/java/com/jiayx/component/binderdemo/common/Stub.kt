package com.jiayx.component.binderdemo.common

import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.os.RemoteException
import android.util.Log
import com.jiayx.component.binderdemo.bean.Person
import com.jiayx.component.binderdemo.interfaces.IPersonManager

abstract class Stub : Binder(), IPersonManager {
    override fun asBinder(): IBinder {
        return this
    }

    @Throws(RemoteException::class)
    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        when (code) {
            INTERFACE_TRANSACTION -> {
                reply?.writeString(DESCRIPTOR)
                return true
            }
            TRANSACTION_addPerson -> {
                Log.e("jia_binder", "Stub,TRANSACTION_addPerson: " + Thread.currentThread())
                data.enforceInterface(DESCRIPTOR)
                var arg0: Person? = null
                if (0 != data.readInt()) {
                    arg0 = Person.CREATOR.createFromParcel(data)
                }
                this.addPerson(arg0)
                reply?.writeNoException()
                return true
            }
            TRANSACTION_getPersonList -> {
                data.enforceInterface(DESCRIPTOR)
                val result: List<Person?>? = this.getPersonList()
                reply?.writeNoException()
                reply?.writeTypedList(result)
                return true
            }
        }
        return super.onTransact(code, data, reply, flags)
    }

    init {
        attachInterface(this, DESCRIPTOR)
    }

    companion object {
        private const val DESCRIPTOR = "com.enjoy.binder.common.IPersonManager"
        fun asInterface(binder: IBinder?): IPersonManager? {
            if (binder == null) {
                return null
            }
            val iin = binder.queryLocalInterface(DESCRIPTOR)
            return if (iin != null && iin is IPersonManager) {
                iin as IPersonManager?
            } else Proxy(binder)
        }

        const val TRANSACTION_addPerson = FIRST_CALL_TRANSACTION
        const val TRANSACTION_getPersonList = FIRST_CALL_TRANSACTION + 1
    }
}