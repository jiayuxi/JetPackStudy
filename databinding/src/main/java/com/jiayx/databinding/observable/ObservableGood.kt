package com.jiayx.databinding.observable

import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat

/**
 *Created by yuxi_
on 2022/5/2
dataBinding 双向绑定 ， 绑定变量的方式比单向绑定多了一个等号： android:text="@={goods.name}"
 */

class ObservableGood {
    val nameField: ObservableField<String> by lazy {
        ObservableField()
    }

    constructor(name: String?) {
        this.nameField.set(name)
    }

}