package com.jiayx.singlecase.single;

import android.util.Log;

/**
 * Created by yuxi_
 * on 2022/3/27
 * 枚举单例模式
 */
public enum SingletonObject06 {
    INSTANCE;

    public void show(){
        System.out.println("show:java  枚举单例模式");
    }
}
