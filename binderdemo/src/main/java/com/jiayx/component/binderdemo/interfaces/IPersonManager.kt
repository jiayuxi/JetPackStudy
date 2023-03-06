package com.jiayx.component.binderdemo.interfaces

import android.os.IInterface
import android.os.RemoteException
import com.jiayx.component.binderdemo.bean.Person

/**
 *  author : Jia yu xi
 *  date : 2023/3/6 18:11:11
 *  description :
 */
interface IPersonManager : IInterface {
    @Throws(RemoteException::class)
    fun addPerson(persons: Person?)

    @Throws(RemoteException::class)
    fun getPersonList(): List<Person?>?
}