package com.jiayx.livedata.event

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.jetbrains.annotations.NotNull
import java.lang.reflect.Field

/**
 *Created by yuxi_
on 2022/5/1
 */
class LiveDataBus {
    // 饿汉式双重检索
    companion object {
        val instance: LiveDataBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LiveDataBus()
        }
    }

    private val bus by lazy { mutableMapOf<String, MyMutableLiveData<Any>>() }

    fun <T> with(key: String, type: Class<T>): MyMutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus += key to MyMutableLiveData<Any>()
        }
        return bus[key] as MyMutableLiveData<T>
    }

    fun with(target: String): MyMutableLiveData<Any> {
        return with(target, Any::class.java)
    }

    fun remove(key: String) {
        if (bus.containsKey(key)) {
            bus.remove(key)
        }
    }

    fun <T> post(key: String, t: T) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            with(key).value = t
        } else {
            with(key).postValue(t)
        }
    }

    class MyMutableLiveData<T> : MutableLiveData<T>() {
        override fun observe(@NotNull owner: LifecycleOwner, @NotNull observer: Observer<in T>) {
            super.observe(owner, observer)
            hook(observer)
        }

        // 处理粘性事件
        fun observerSticky(@NotNull owner: LifecycleOwner, @NotNull observer: Observer<in T>) {
            super.observe(owner, observer)
        }

        private fun hook(@NotNull observer: Observer<in T>) {
            try {
                val liveDataClass = LiveData::class.java
                val mObserversField = liveDataClass.getDeclaredField("mObservers")
                mObserversField.isAccessible = true
                val mObserversObject = mObserversField.get(this)
                val mObserversObjectClass = mObserversObject.javaClass
                //获取到mObservers对象的get方法
                val getMethod = mObserversObjectClass.getDeclaredMethod("get", Any::class.java)
                getMethod.isAccessible = true
                //执行get方法
                val invokeEntry = getMethod.invoke(mObserversObject, observer)
                var observerWraper: Any? = null
                if (invokeEntry != null && invokeEntry is Map.Entry<*, *>) {
                    observerWraper = invokeEntry.value
                }
                if (observerWraper == null) {
                    throw NullPointerException("observerWraper 为空")
                }
                val superclass: Class<*> = observerWraper.javaClass.superclass
                val mLastVersion: Field = superclass.getDeclaredField("mLastVersion")
                mLastVersion.isAccessible = true
                val mVersion = liveDataClass.getDeclaredField("mVersion")
                mVersion.isAccessible = true
                val mVersionValue: Any = mVersion.get(this)
                mLastVersion.set(observerWraper, mVersionValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
