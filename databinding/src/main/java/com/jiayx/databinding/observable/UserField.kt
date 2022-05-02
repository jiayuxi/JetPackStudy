package com.jiayx.databinding.observable

import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat

/**
 *Created by yuxi_
on 2022/5/2
实现数据变化自动驱动 UI 刷新的方式有三种：BaseObservable、ObservableField、ObservableCollection
单项数据绑定
 */
class UserField {
    val nameField: ObservableField<String> by lazy {
        ObservableField()
    }
    val details: ObservableField<String> by lazy {
        ObservableField()
    }
    val pcieField: ObservableFloat by lazy {
        ObservableFloat()
    }

    constructor(name: String?, details: String?, price: Float) {
        this.nameField.set(name)
        this.details.set(details)
        this.pcieField.set(price)
    }


}