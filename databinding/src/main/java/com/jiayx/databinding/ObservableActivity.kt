package com.jiayx.databinding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jiayx.databinding.databinding.ActivityObserableBinding
import com.jiayx.databinding.observable.UserViewModel
import java.util.*


/**
 *Created by yuxi_
on 2022/5/2
 */
class ObservableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObserableBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_obserable)
        userViewModel = UserViewModel("李四", "kotlin", 26f)
        binding.userViewModel = userViewModel
        binding.goodsHandler = GoodHandler()

    }

    inner class GoodHandler {
        fun changeGoodsName() {
            userViewModel.name = ("code" + Random().nextInt(100))
            userViewModel.setPrice(Random().nextInt(100).toFloat())
        }

        fun changeGoodsDetails(view: View) {
            userViewModel.setDetails("hi" + Random().nextInt(100))
            userViewModel.setPrice(Random().nextInt(100).toFloat())
        }
    }
}