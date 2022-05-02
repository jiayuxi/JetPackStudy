package com.jiayx.databinding.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 *Created by yuxi_
on 2022/5/1

 */
open class ImageViewBindingAdapter {

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

        @JvmStatic
        @BindingAdapter(value = ["android:text"])
        fun setText(view: Button, text: String) {
            view.text = "$text - Button"
        }

       /* @BindingConversion
        @JvmStatic
        fun convertStringToDrawable(str: String): Drawable? {
            return when (str) {
                "红色" -> ColorDrawable(Color.parseColor("#FF4081"))
                "蓝色" -> ColorDrawable(Color.parseColor("#3F51B5"))
                else -> ColorDrawable(Color.parseColor("#344567"))
            }
        }


        @BindingConversion
        @JvmStatic
        fun convertStringToColor(str: String): Int {
            return when (str) {
                "红色" -> Color.parseColor("#FF4081")
                "蓝色" -> Color.parseColor("#3F51B5")
                else -> Color.parseColor("#344567")
            }
        }*/
    }
}