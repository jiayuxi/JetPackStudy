package com.jiayx.singlecase.single;

import android.util.Log;

/**
 * Created by yuxi_
 * on 2022/3/27
 * Java 单例模式 饿汉式
 */
public class SingletonObject01 {
    private static final SingletonObject01 instance = new SingletonObject01();
    private SingletonObject01(){

    }
    public static SingletonObject01 getInstance(){
        return instance;
    }

    public void show(){
        System.out.println("java 饿汉式单例模式");
    }
}
