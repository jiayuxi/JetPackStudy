package com.jiayx.component.annotation.view

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 *  author : Jia yu xi
 *  date : 2023/3/3 22:11:11
 *  description :
 */
object InjectUtils {

    fun injectEvent(activity: AppCompatActivity?) {
        activity ?: return
        // 获取class 类的对象
        val javaClass: Class<out Activity> = activity.javaClass
        // 获取 class 对象里的所有的方法
        val declaredMethods = javaClass.declaredMethods
        // 遍历素有的方法
        for (method in declaredMethods) {
            //获得方法上所有注解
            val annotations = method.annotations
            for (annotation: Annotation in annotations) {
                //注解类型
                val annotationType = annotation.javaClass.componentType
                // 判断是否是指定的注解类型
                if (annotationType.isAnnotationPresent(EventType::class.java)) {
                    // 获取注解类型
                    val eventType: EventType =
                        annotationType.getAnnotation(EventType::class.java) as EventType
                    // OnClickListener.class
                    val listenerType: Class<*> = eventType.listenerType.java
                    // setOnClickListener
                    val listenerSetter = eventType.listenerSetter
                    try {
                        // 得到注解的所有方法
                        val valueMethod = annotationType.getDeclaredMethod("value")
                        val viewIds = valueMethod.invoke(annotation) as IntArray
                        method.isAccessible = true
                        val handler = ListenerInvocationHandler(activity, method)
                        val proxyInstance = Proxy.newProxyInstance(
                            listenerType::class.java.classLoader,
                            arrayOf<Class<*>>(listenerType::class.java),
                            handler
                        )
                        // 遍历注解的值
                        for (viewId in viewIds) {
                            // 获得当前activity的view（赋值）
                            val view = activity.findViewById<View>(viewId)
                            // 获取指定的方法(不需要判断是Click还是LongClick)
                            // 如获得：setOnClickListener方法，参数为OnClickListener
                            // 获得 setOnLongClickListener，则参数为OnLongClickListener
                            val setter = view.javaClass.getMethod(listenerSetter, listenerType)
                            setter.invoke(view, proxyInstance)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * 还可能在自定义view注入，所以是泛型： T = Activity/View
     *
     * @param <T>
    </T> */
    internal class ListenerInvocationHandler<T>(private val target: T, private val method: Method) :
        InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
            return this.method.invoke(target, *args)
        }
    }
}