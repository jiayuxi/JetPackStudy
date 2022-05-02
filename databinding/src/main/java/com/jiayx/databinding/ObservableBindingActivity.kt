package com.jiayx.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jiayx.databinding.databinding.ActivityObservableBindingBinding
import com.jiayx.databinding.observable.ObservableGood

/**
 *Created by yuxi_
on 2022/5/2
双向绑定
 */
class ObservableBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ActivityObservableBindingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_observable_binding)
        val userField = ObservableGood("赵四")
        dataBinding.observableGood = userField
    }
}