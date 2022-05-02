package com.jiayx.databinding.observable

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.jiayx.databinding.BR

/**
 *Created by yuxi_
on 2022/5/2
实现数据变化自动驱动 UI 刷新的方式有三种：BaseObservable、ObservableField、ObservableCollection
单项数据绑定
 */
class UserViewModel : BaseObservable {
    // 如果是 public 修饰，则可以在成员变量上直接加上@Bindable注解
    @Bindable
    var name: String? = null
        set(value) {
            field = value
            //只更新本字段
            notifyPropertyChanged(BR.name)
        }

    // 如果是 private 修饰，则在成员变量的 get 方法上加上@Bindable注解
    private var details: String? = null

    private var price: Float = 0f

    constructor(name: String?, details: String?, price: Float) {
        this.name = name
        this.details = details
        this.price = price
    }

    @Bindable
    fun getDetails(): String? {
        return details
    }

    fun setDetails(details: String?) {
        this.details = details
        //更新所有字段
        notifyChange()
    }

    fun getPrice(): Float {
        return price
    }

    fun setPrice(price: Float) {
        this.price = price
    }
}