package com.jiayx.component.viewpage.view

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

/**
 *  author : Jia yu xi
 *  date : 2022/12/20 11:11:11
 *  description :
 */
class CustomViewPage(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) {
                height = h
            }
        }
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}