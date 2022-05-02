package com.jiayx.databinding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jiayx.databinding.databinding.ActivityObservableFieldBinding
import com.jiayx.databinding.observable.UserField
import com.jiayx.databinding.util.GoodHandler

/**
 *Created by yuxi_
on 2022/5/2
 */
class ObservableFieldActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ActivityObservableFieldBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_observable_field)
        dataBinding.lifecycleOwner = this
        val userField = UserField("赵四", "赵四的霹雳舞", 56f)
        dataBinding.userField = userField
        dataBinding.goodsHandler = GoodHandler(userField)
    }
}