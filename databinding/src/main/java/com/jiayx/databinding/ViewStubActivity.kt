package com.jiayx.databinding

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import com.jiayx.databinding.bean.User
import com.jiayx.databinding.databinding.ActivityViewStubBinding
import com.jiayx.databinding.databinding.SubBinding


/**
 *Created by yuxi_
on 2022/5/2
 */
class ViewStubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ActivityViewStubBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_view_stub)
        val userField = User("赵四", 56)
        dataBinding.user = userField
        val view1: View? = dataBinding.viewStub1.viewStub?.inflate()


        //如果在 xml 中没有使用 bind:userInfo="@{userInf}" 对 ViewStub 进行数据绑定，
        // 则可以等到当 ViewStub Inflate 时再绑定变量，此时需要为 ViewStub 设置
        // setOnInflateListener回调函数，在回调函数中进行数据绑定
        // 1、调用顺序有关
        dataBinding.viewStub2.setOnInflateListener(ViewStub.OnInflateListener { stub, inflated ->
            //如果在 xml 中没有使用 app:userInfo="@{userInf}" 对 viewStub 进行数据绑定
            //那么可以在此处进行手动绑定
            val userField = User("hello", 20)
            val viewStubBinding: SubBinding? = DataBindingUtil.bind(inflated)
            viewStubBinding?.user = userField
        })
        // 2、
        val view2: View? = dataBinding.viewStub2.viewStub?.inflate()
    }
}