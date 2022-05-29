package com.jiayx.jetpackstudy.paging3.utils

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import coil.size.Scale

/**
 *Created by yuxi_
on 2022/5/29
 */
class ImageBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["loadImage", "placeHolder", "errorImage"], requireAll = false)
        fun setImage(imageview: ImageView, url: String?, default: Drawable, error: Drawable) {
            if (!TextUtils.isEmpty(url)) {
                imageview.load(url) {
                    //磁盘缓存策略
                    diskCachePolicy(CachePolicy.ENABLED)
                    //渐进渐出
                    crossfade(true)
                    //渐进渐出的时间
                    crossfade(1500)
                    //placeholder预置展位图
                    placeholder(default)
                    // 加载失败显示的图片
                    error(error)
                }
            } else {
                imageview.load(default)
            }
        }
    }
}