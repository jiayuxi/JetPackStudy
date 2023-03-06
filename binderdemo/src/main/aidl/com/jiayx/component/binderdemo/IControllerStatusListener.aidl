// IControllerStatusListener.aidl
package com.jiayx.component.binderdemo;

// Declare any non-default types here with import statements

interface IControllerStatusListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
       void onPauseSuccess();
       void onPauseFailed(int errorCode);
       void onPlaySuccess();
       void onPlayFailed(int errorCode);
}