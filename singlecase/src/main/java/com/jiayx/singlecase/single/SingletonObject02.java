package com.jiayx.singlecase.single;

import android.util.Log;

/**
 * Created by yuxi_
 * on 2022/3/27
 * 饿汉式 单例模式
 */
public class SingletonObject02 {
    private static SingletonObject02 ourInstance;

    public static SingletonObject02 getInstance() {
        if (ourInstance == null) {
            ourInstance = new SingletonObject02();
        }
        return ourInstance;
    }

    private SingletonObject02() {
    }

    public void show() {
        System.out.println("show: java 饿汉式单例模式");
    }
}
