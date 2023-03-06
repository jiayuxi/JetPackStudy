package com.jiayx.component.binderdemo.common

import android.os.IBinder
import android.os.Parcel
import android.os.RemoteException
import com.jiayx.component.binderdemo.bean.Person
import com.jiayx.component.binderdemo.common.Stub.Companion.TRANSACTION_addPerson
import com.jiayx.component.binderdemo.interfaces.IPersonManager

/**
 *  author : Jia yu xi
 *  date : 2023/3/6 20:17:17
 *  description :
 */
class Proxy(private val mRemote: IBinder?) : IPersonManager {
    companion object {
        private const val DESCRIPTOR = "com.enjoy.binder.common.IPersonManager"
    }

    @Throws(RemoteException::class)
    override fun addPerson(persons: Person?) {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            data.writeInterfaceToken(DESCRIPTOR)
            persons?.let {
                data.writeInt(1)
                it.writeToParcel(data, 0)
            } ?: kotlin.run {
                data.writeInt(0)
            }
            mRemote?.transact(TRANSACTION_addPerson, data, reply, 0)
            reply.readException()
        } finally {
            data.recycle()
            reply.recycle()
        }
    }

    @Throws(RemoteException::class)
    override fun getPersonList(): List<Person?>? {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        val result: List<Person?>?
        try {
            data.writeInterfaceToken(DESCRIPTOR)
            mRemote?.transact(Stub.TRANSACTION_getPersonList, data, reply, 0)
            reply.readException()
            result = reply.createTypedArrayList(Person.CREATOR)
        } finally {
            reply.recycle()
            data.recycle()
        }
        return result
    }

    override fun asBinder(): IBinder? {
        return mRemote
    }
}
