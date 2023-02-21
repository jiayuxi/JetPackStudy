package com.jiayx.bluetooth.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    @Suppress("PropertyName")
    open val TAG: String = "LOG${javaClass.simpleName}"
    abstract fun onKeyEventListener(code: Int)
    private var takeKeyTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)//去掉标题
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//设置背景透明
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val attributes = window?.attributes
        attributes?.alpha = 0.7f
        window?.attributes = attributes
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog?.window?.setLayout(
            (dm.widthPixels * 0.6).toInt(), ViewGroup.LayoutParams
                .WRAP_CONTENT
        )
        dialog?.window?.setGravity(Gravity.CENTER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener { _, keyCode, event ->
            Log.d(TAG, "onViewCreated: keyCode: $keyCode , event: $event")
            if (event?.action == KeyEvent.ACTION_UP && event.repeatCount == 0 && keyCode == KeyEvent.KEYCODE_F9) {
                takeKeyTime = event?.downTime!!
            } else if (event?.action == KeyEvent.ACTION_UP && event.repeatCount == 0) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        onKeyEventListener(keyCode)
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        onKeyEventListener(keyCode)
                    }
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        onKeyEventListener(keyCode)
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        onKeyEventListener(keyCode)
                    }
                    KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_ESCAPE -> {
                        Log.d(TAG, "onViewCreated: 长按时间的对比： ${event?.downTime?.minus(takeKeyTime)}")
//                        if ((event?.downTime - takeKeyTime > 1000) && (takeKeyTime != 0L)) {
//                            onKeyEventListener(keyCode)
//                            takeKeyTime = 0
//                        } else {
//                            onKeyEventListener(keyCode)
//                        }
                        onKeyEventListener(keyCode)
                    }
                }
            }
            true
        }
    }
}