package com.jiayx.databinding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiayx.databinding.adapter.ManyRecyclerViewAdapter
import com.jiayx.databinding.adapter.RecyclerViewAdapter
import com.jiayx.databinding.bean.User
import com.jiayx.databinding.databinding.ActivityRecyclerViewBinding

/**
 *Created by yuxi_
on 2022/5/3
 */
class RecyclerViewActivity : AppCompatActivity() {
    private val list by lazy { arrayListOf<User>() }
    private var adapterItem: ManyRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        val binding: ActivityRecyclerViewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_recycler_view)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapterItem = ManyRecyclerViewAdapter(list)
        binding.recyclerView.adapter = adapterItem
        adapterItem?.notifyDataSetChanged()
        binding.click = ClickBtn()
    }

    private fun initData() {
        for (i in 0..9) {
            list.add(User(i.toString(), i + 20))
        }
    }

    private fun updateData() {
        for (i in 10..19) {
            list.add(User(i.toString(), i + 20))
        }
    }

    inner class ClickBtn {
        fun onClick(view: View) {
            updateData()
            adapterItem?.notifyDataSetChanged()
        }
    }
}