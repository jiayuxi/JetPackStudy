package com.jiayx.coil.tool

import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.load

/**
 *Created by yuxi_
on 2022/5/3
 */
class ImageBindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["loadImage", "placeHolder", "errorImage"], requireAll = false)
        fun setImage(imageview: ImageView, url: String?, default: Drawable, error: Drawable) {
            if (!TextUtils.isEmpty(url)) {
                Log.d("jia_coil", "setImage: url : $url")
                imageview.load(url) {
                    //placeholder预置展位图
                    placeholder(default)
                    // 预览展位的时间
                    crossfade(3000)
                    // 加载失败显示的图片
                    error(error)
                }
//                Glide.with(imageview.context)
//                    .load(url)
//                    .apply(RequestOptions().placeholder(default).error(error))
//                    .into(imageview)
            } else {
                imageview.load(default)
            }
        }

        @JvmStatic
        @BindingAdapter(
            value = ["gifLoadImage", "gifPlaceHolder", "gifErrorImage"],
            requireAll = false
        )
        fun setGifImage(imageView: ImageView, url: String, placeholder: Int, error: Int) {
            if (!TextUtils.isEmpty(url)) {
                val imageLoader = ImageLoader.Builder(imageView.context)
                    .components {
                        if (SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }.build()
//                //设置全局唯一的实例
//                Coil.setImageLoader(imageLoader)
                imageView.load(url, imageLoader) {
                    placeholder(placeholder)
                    error(error)
                }
            } else {
                imageView.load(placeholder)
            }
        }

        @JvmStatic
        @BindingAdapter(
            value = ["svgLoadImage", "svgPlaceHolder", "svgErrorImage"],
            requireAll = false
        )
        fun setSvgImage(
            imageView: ImageView,
            loadImage: Int,
            placeholder: Int,
            error: Int
        ) {
            val imageLoader = ImageLoader.Builder(imageView.context)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()

            imageView.load(loadImage, imageLoader) {
                placeholder(placeholder)
                error(error)
            }
        }

        @JvmStatic
        @BindingAdapter(
            value = ["svgLoadImageUrl", "svgPlaceHolder", "svgErrorImage"], requireAll = false
        )
        fun setSvgImageUrl(
            imageView: ImageView,
            url: String,
            placeholder: Int,
            error: Int
        ) {
            if (!TextUtils.isEmpty(url)) {
                val imageLoader = ImageLoader.Builder(imageView.context)
                    .components {
                        add(SvgDecoder.Factory())
                    }
                    .build()
                imageView.load(url, imageLoader) {
                    placeholder(placeholder)
                    error(error)
                }
            } else {
                imageView.load(placeholder)
            }
        }
    }
}