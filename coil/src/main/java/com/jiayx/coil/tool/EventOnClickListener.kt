package com.jiayx.coil.tool

import android.content.Context
import android.content.Intent
import android.view.View
import com.jiayx.coil.GifActivity
import com.jiayx.coil.PictureListActivity
import com.jiayx.coil.SvgActivity

/**
 *Created by yuxi_
on 2022/5/3
 */
class EventOnClickListener(private val context: Context) {

    fun startListActivity(view: View) {
        context.startActivity(Intent(context, PictureListActivity::class.java))
    }

    fun startSvgActivity(view: View) {
        context.startActivity(Intent(context, SvgActivity::class.java))
    }

    fun startGifActivity(view: View) {
        context.startActivity(Intent(context, GifActivity::class.java))
    }
}