package com.jiayx.databinding.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions

/**
 *Created by yuxi_
on 2022/5/1

 */
class ImageViewBindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["loadImage"])
        fun setImage(imageView: ImageView?, url: String?) {
            if (!TextUtils.isEmpty(url)) {
                imageView?.let {
                    Glide.with(it)
                        .load(url)
                        .into(it)
                }
            } else {
                imageView?.let {
                    it.setBackgroundColor(Color.GRAY)
                }
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["loadImage"])
        fun setImage(imageView: ImageView?, localInt: Int) {
            imageView?.let {
                it.setImageResource(localInt)
            }
        }

        @JvmStatic
        @BindingAdapter(
            value = ["placeHolder", "error", "loadImage", "defaultImage"],
            requireAll = false
        )
        // 多参数
        fun setImage(
            imageView: ImageView?,
            placeHolder: Int,
            error: Int,
            url: String?,
            localInt: Int
        ) {
            if (!TextUtils.isEmpty(url)) {
                imageView?.let {
                    Glide.with(it)
                        .load(url)
                        .apply(RequestOptions().placeholder(placeHolder).error(error))
                        .into(it)
                }
            } else {
                imageView?.let {
                    it.setImageResource(localInt)
                }
            }
        }
    }
}