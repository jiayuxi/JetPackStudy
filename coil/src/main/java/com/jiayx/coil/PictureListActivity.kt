package com.jiayx.coil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiayx.coil.adapter.PictureAdapter
import com.jiayx.coil.databinding.ActivityListBinding
import com.jiayx.coil.viewmodel.PictureViewModel

/**
 *Created by yuxi_
on 2022/5/3
 */
class PictureListActivity : AppCompatActivity() {
    private val viewModel: PictureViewModel by lazy {
        ViewModelProvider(this).get(PictureViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_list)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        val adapter = PictureAdapter()
        binding.recycler.adapter = adapter
        adapter.setData(viewModel.arrayList)
    }
}