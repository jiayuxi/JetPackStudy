package com.jiayx.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jiayx.databinding.databinding.ActivityViewmodelBinding
import com.jiayx.databinding.viewmodel.DataViewModel

/**
 *Created by yuxi_
on 2022/5/2
 */
class ViewModelActivity : AppCompatActivity() {
    private val viewModel: DataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityViewmodelBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_viewmodel)
        binding.lifecycleOwner = this

        binding.viewmodel = viewModel
    }
}