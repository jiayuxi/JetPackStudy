package com.jiayx.databinding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import com.jiayx.databinding.databinding.ActivityObservableCollectionBinding
import java.util.*

/**
 *Created by yuxi_
on 2022/5/2
实现数据变化自动驱动 UI 刷新的方式有三种：BaseObservable、ObservableField、ObservableCollection
单项数据绑定
 */
class ObservableCollectionActivity : AppCompatActivity() {
    private lateinit var map:ObservableMap<String,String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityObservableCollectionBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_observable_collection)
        map = ObservableArrayMap()
        map["name"] = "赵柳"
        map["age"] =  "30"
        binding.map = map
        val arrayList = ObservableArrayList<String>()
        arrayList.add("王五")
        arrayList.add("赵柳")
        arrayList.add("赵吧")
        binding.list = arrayList
        binding.index = 2
        binding.key = "name"
    }

    fun onClick(view: View) {
        map["name"] = "leavesC,hi" + Random().nextInt(100)
    }
}