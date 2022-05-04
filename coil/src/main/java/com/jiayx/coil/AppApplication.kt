package com.jiayx.coil

import android.app.Application
import android.content.Context
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.memory.MemoryCache
import coil.request.CachePolicy
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

/**
 *Created by yuxi_
on 2022/5/3
 */
class AppApplication : Application() {
    //CachePolicy.ENABLED : 可读可写
    //CachePolicy.READ_ONLY : 只读
    //CachePolicy.WRITE_ONLY : 只写
    //CachePolicy.DISABLED : 不可读不可写，即禁用
    override fun onCreate() {
        super.onCreate()
        //设置全局唯一的实例
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .memoryCache { MemoryCache.Builder(this).maxSizePercent(0.25).build() }
                //设置内存缓存策略 ，可读可写
                .memoryCachePolicy(CachePolicy.ENABLED)
                //设置磁盘缓存策略
                .diskCachePolicy(CachePolicy.ENABLED)
                //设置网络缓存策略
                .networkCachePolicy(CachePolicy.ENABLED)
                //渐进渐出
                .crossfade(true)
                // 预览展位的时间
                .crossfade(3000)
                //placeholder预置展位图
                .placeholder(R.mipmap.picture3)
                // 加载失败显示的图片
                .error(R.mipmap.picture4)
//                .okHttpClient(createOkHttp(this))
                .components {
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(SvgDecoder.Factory())
                }.build()
        )
    }

    private fun createOkHttp(application: Application): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(createDefaultCache(application))
            .build()
    }

    private fun createDefaultCache(context: Context): Cache {
        val cacheDirectory = getDefaultCacheDirectory(context)
        return Cache(cacheDirectory, 10 * 1024 * 1024)
    }

    private fun getDefaultCacheDirectory(context: Context): File {
        return File(context.cacheDir, "image_cache").apply { mkdirs() }
    }
}