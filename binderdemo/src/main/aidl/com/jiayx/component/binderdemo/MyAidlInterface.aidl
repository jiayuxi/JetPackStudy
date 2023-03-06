// MyAidlInterface.aidl
package com.jiayx.component.binderdemo;
import com.jiayx.component.binderdemo.IControllerStatusListener;

// Declare any non-default types here with import statements

interface MyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    void pause(String pause);
    void play(String paly);
    void setControllerStatusListener(IControllerStatusListener i);
}