package com.jiayx.singlecase.single;

import android.util.Log;

/**
 * Created by yuxi_
 * on 2022/3/27
 * 双重检索的线程安全单例
 */
public class SingletonObject04 {
    private volatile static SingletonObject04 ourInstance;

    public static SingletonObject04 getInstance() {
        if (ourInstance == null) {
            synchronized (SingletonObject04.class) {
                if (ourInstance == null) {
                    ourInstance = new SingletonObject04();
                }
            }
        }
        return ourInstance;
    }

    private SingletonObject04() {
    }

    public void show() {
        System.out.println("show: java 线程安全的 双重检索单例摸");
    }
}
