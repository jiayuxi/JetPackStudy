package com.jiayx.singlecase.single;

/**
 * Created by yuxi_
 * on 2022/3/27
 * 静态内部类的单例模式
 */
public class SingletonObject05 {
    public static class SingletonHelp {
        public static final SingletonObject05 INSTANCE = new SingletonObject05();
    }

    public static SingletonObject05 getInstance() {
        return SingletonHelp.INSTANCE;
    }

    public SingletonObject05() {
    }
    // 每次创建一个新的对象
    public synchronized static SingletonObject05 singletonObject05() {
        return new SingletonObject05();
    }

    public void show() {
        System.out.println("show: java 静态内部类 单例模式");
    }
}
