package com.jiayx.coil

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.result
import coil.util.CoilUtils
import com.jiayx.coil.databinding.ActivityDownloadBinding
import okhttp3.OkHttpClient

/**
 *Created by yuxi_
on 2022/5/3
 */
class DownloadActivity : AppCompatActivity() {
    private var binding: ActivityDownloadBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download)
        binding?.onclick = EventOnclick()
    }

    inner class EventOnclick {
        // 监听下载 与 取消操作
        fun downloadOrCancel(view: View) {
            val disposable =
                binding?.image?.load("https://t7.baidu.com/it/u=433422559,1779762296&fm=193&f=GIF") {
                    allowHardware(false)
//                    memoryCachePolicy(CachePolicy.DISABLED)
                    diskCachePolicy(CachePolicy.DISABLED)
                    crossfade(true)
                    crossfade(3000)
                    placeholder(R.mipmap.picture3)
                    error(R.mipmap.picture4)
                    listener(
                        onStart = { request ->
                            Log.d("coil-", "onStart")
                        },
                        onError = { request, throwable ->
                            Log.d("coil-", "onError")
                        },
                        onCancel = { request ->
                            Log.d("coil-", "onCancel")
                            Toast.makeText(this@DownloadActivity, "取消图片加载", Toast.LENGTH_SHORT)
                                .show()
                            binding?.image?.setImageResource(0)
                        },
                        onSuccess = { _, result ->
                            Log.d("coil-", "onSuccess : ${result.memoryCacheKey}")
                            //将MemoryCache.Key起始图像的 用作placeholderMemoryCacheKey结束图像。
                            // 这样可以确保将开始图像用作结束图像的占位符，如果图像在内存缓存中，
                            // 则可以实现平滑过渡，不会出现白色闪烁。
                            binding?.image?.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.lemeitu.com%2Fm00%2F8b%2F3e%2F0cc04c9a2b4fa8da0bea4c77f089831c__w.jpg&refer=http%3A%2F%2Fimg.lemeitu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158332&t=c0fd0781e2e22c1efc1780c5de886516") {
                                placeholderMemoryCacheKey(result.memoryCacheKey)
                            }
                        }
                    )
                }
            // 取消加载
//            disposable?.dispose()
        }

        // 替换okhttp
        fun okhttp(view: View) {
            val okHttpClient = OkHttpClient.Builder()
                .build()
            val imageLoader = ImageLoader.Builder(view.context).okHttpClient {
                okHttpClient
            }.build()
            Coil.setImageLoader(imageLoader)
            binding?.image?.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.2008php.com%2F09_Website_appreciate%2F10-07-11%2F1278861720_g.jpg&refer=http%3A%2F%2Fwww.2008php.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654010430&t=e5cdc712f1ab52eba269bee9fbeae0e1")
        }

        //自定义
        fun custom(view: View) {
            val okHttpClient = OkHttpClient.Builder()
                .build()
            val imageLoader = ImageLoader.Builder(view.context)
                .diskCachePolicy(CachePolicy.ENABLED)  //磁盘缓策略 ENABLED、READ_ONLY、WRITE_ONLY、DISABLED
                .crossfade(true) //淡入淡出
                .crossfade(1000)  //淡入淡出时间
                .okHttpClient {  //设置okhttpClient实例
                    okHttpClient
                }.build()
            Coil.setImageLoader(imageLoader)
            binding?.image?.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Faea02e2bf87ec19b386862baf5b6f0b2a1fa358161fed-10PDle_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158548&t=aa4f18eaccb94546785fac3408b3ea50") {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}