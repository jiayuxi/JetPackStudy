package com.jiayx.coil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jiayx.coil.databinding.ActivityGifBinding

/**
 *Created by yuxi_
on 2022/5/3
 */
class GifActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityGifBinding = DataBindingUtil.setContentView(this,R.layout.activity_gif)
        binding.loadUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F080c6e3b56c0b373ac8d1fb97ffa55884d464202182cd3-2DnAYj_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654161043&t=ea9e3bf9b84ce72a07fd3cc5d6287c44"
        binding.placeHolder = R.drawable.picture2
        binding.error = R.drawable.picture1
        binding.loadUrl2 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbqb12.bingping.top%2FUploads%2Fvod%2F2018-08-07%2F5b690f30cbe43.jpg&refer=http%3A%2F%2Fbqb12.bingping.top&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654162676&t=eb37a43a556e3f6140a9367e53b7c549"
        binding.loadUrl3 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.clhweb.com%2Fupload%2F2018%2F0201%2Fsvg-animate-along-path.gif&refer=http%3A%2F%2Fwww.clhweb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654161201&t=44d405a75338751a8c9f9a67dda8f344"
    }
}