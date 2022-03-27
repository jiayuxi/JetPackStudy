package com.jiayx.singlecase.single;

import android.util.Log;

/**
 * Created by yuxi_
 * on 2022/3/27
 * 线程安全的 饿汉式单例模式
 */
public class SingletonObject03 {
    private static SingletonObjectKt03 ourInstance;

    public static synchronized SingletonObjectKt03 getInstance() {
        if (ourInstance == null) {
            ourInstance = new SingletonObjectKt03();
        }
        return ourInstance;
    }

    private SingletonObject03() {
    }

    public void show(){
        System.out.println("show: java 线程安全的 单例模式");
    }
}
