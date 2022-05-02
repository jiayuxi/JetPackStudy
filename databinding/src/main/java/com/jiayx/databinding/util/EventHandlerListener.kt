package com.jiayx.databinding.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.jiayx.databinding.ObservableActivity
import com.jiayx.databinding.ObservableCollectionActivity
import com.jiayx.databinding.ObservableFieldActivity

/**
 *Created by yuxi_
on 2022/5/1
 */
class EventHandlerListener(private val context: Context) {

    fun buttonOnClick(view: View) {
        Toast.makeText(context, "点击事件", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, ObservableActivity::class.java))
    }
    fun button2OnClick(view: View) {
        context.startActivity(Intent(context, ObservableFieldActivity::class.java))
    }
    fun button3OnClick(view: View) {
        context.startActivity(Intent(context, ObservableCollectionActivity::class.java))
    }
}