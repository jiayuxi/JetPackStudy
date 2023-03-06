package com.jiayx.component.binderdemo.impl

import android.os.IBinder
import com.jiayx.component.binderdemo.IControllerStatusListener
import com.jiayx.component.binderdemo.MyAidlInterface


/**
 *  author : Jia yu xi
 *  date : 2023/3/6 23:35:35
 *  description :
 */
abstract class MyAidlInterfaceImpl : MyAidlInterface.Stub() {

    override fun pause(pause: String?) {
    }

    override fun play(paly: String?) {
    }

    override fun setControllerStatusListener(i: IControllerStatusListener?) {

    }
}