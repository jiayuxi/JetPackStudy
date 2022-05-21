package com.jiayx.gesturedetector.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.jiayx.gesturedetector.R


/**
 *Created by yuxi_
on 2022/5/21
 */
class ShowChangeLayout(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private var iv_center: ImageView? = null
    private var pb: ProgressBar? = null
    private var mHideRunnable: HideRunnable? = null
    private var duration = 1000

    init {
        LayoutInflater.from(context).inflate(R.layout.show_change_layout, this)
        iv_center = findViewById<View>(R.id.iv_center) as ImageView
        pb = findViewById<View>(R.id.pb) as ProgressBar
        mHideRunnable = HideRunnable()
        this@ShowChangeLayout.visibility = GONE
    }

    //显示
    fun show() {
        visibility = VISIBLE
        removeCallbacks(mHideRunnable)
        postDelayed(mHideRunnable, duration.toLong())
    }

    //设置进度
    fun setProgress(progress: Int) {
        pb?.progress = progress
        Log.d("jia_gesture", "setProgress: $progress")
    }

    //设置持续时间
    fun setDuration(duration: Int) {
        this.duration = duration
    }

    //设置显示图片
    fun setImageResource(resource: Int) {
        iv_center?.setImageResource(resource)
    }

    //隐藏自己的Runnable
    private inner class HideRunnable : Runnable {
        override fun run() {
            this@ShowChangeLayout.visibility = GONE
        }
    }
}