package com.jiayx.component.annotation.view

import android.view.View
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(
    RetentionPolicy.RUNTIME
)
@EventType(
    listenerType = View.OnLongClickListener::class,
    listenerSetter = "setOnLongClickListener"
)
annotation class OnLongClick(vararg val value: Int)