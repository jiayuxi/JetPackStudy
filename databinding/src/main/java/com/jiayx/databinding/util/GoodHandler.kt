package com.jiayx.databinding.util

import android.view.View
import com.jiayx.databinding.observable.UserField
import com.jiayx.databinding.observable.UserViewModel
import java.util.*

/**
 *Created by yuxi_
on 2022/5/2
 */
class GoodHandler(private val userField: UserField) {
    fun changeGoodsName() {
//        userField.nameField.set("code" + Random().nextInt(100))
//        userField.pcieField.set(Random().nextInt(100).toFloat())
        userField.nameField.value = ("code" + Random().nextInt(100))
        userField.pcieField.value = (Random().nextInt(100).toFloat())

    }

    fun changeGoodsDetails(view: View) {
//        userField.details.set("hi" + Random().nextInt(100))
//        userField.pcieField.set(Random().nextInt(100).toFloat())
        userField.details.value = ("hi" + Random().nextInt(100))
        userField.pcieField.value = (Random().nextInt(100).toFloat())
    }
}