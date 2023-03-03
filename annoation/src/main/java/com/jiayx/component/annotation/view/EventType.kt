package com.jiayx.component.annotation.view

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(RetentionPolicy.RUNTIME)
annotation class EventType(val listenerType: KClass<*>, val listenerSetter: String)