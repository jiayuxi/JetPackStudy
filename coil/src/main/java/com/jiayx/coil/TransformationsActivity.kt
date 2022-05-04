package com.jiayx.coil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.jiayx.coil.databinding.ActivityTransformationsBinding

/**
 *Created by yuxi_
on 2022/5/4
 */
class TransformationsActivity : AppCompatActivity() {
    private val loadUrl =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-082005%2Fnh4sg1ggvyqnh4sg1ggvyq.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158241&t=e3d60d81ac5ada9717efe47778343d37"
    private val loadUrl2 =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2Fb%2Fc3%2F14231116369.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158100&t=a815dd183e11618dc498125239ac9aa2"
    private val loadUrl3 =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F2%2Ff6%2F3c151252383.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158346&t=b90097bf7c62be15a22b25f6025a23b5"
    private val loadUrl4 =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-082106%2F1xozklyuhp01xozklyuhp0.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158484&t=73a2ac656301585ba8bc29fa77309af3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTransformationsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_transformations)
        binding.loadUrl = loadUrl
        binding.error = R.mipmap.picture3
        binding.placeHolder = R.drawable.picture2

        binding.image5.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-082005%2Fnh4sg1ggvyqnh4sg1ggvyq.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158241&t=e3d60d81ac5ada9717efe47778343d37") {
            placeholder(R.mipmap.picture4)
            error(R.mipmap.picture3)
            transformations(RoundedCornersTransformation(120f, 120f, 150f, 150f))
//            transformations(CircleCropTransformation())
            scale(Scale.FIT)
        }
        //正常显示
        binding.image2.load(loadUrl2) {
            placeholder(R.mipmap.picture4)
            error(R.mipmap.picture3)
            transformations(RoundedCornersTransformation(0f))
            scale(Scale.FIT)
        }
        // 圆角显示
        binding.image3.load(loadUrl3) {
            placeholder(R.mipmap.picture4)
            error(R.mipmap.picture3)
            transformations(RoundedCornersTransformation(50f))
            scale(Scale.FIT)
        }
        // 非正常圆角 显示
        binding.image4.load(loadUrl4) {
            placeholder(R.mipmap.picture4)
            error(R.mipmap.picture3)
            transformations(RoundedCornersTransformation(120f, 120f, 150f, 150f))
            scale(Scale.FIT)
        }
        // 圆型显示
        binding.image5.load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-082005%2Fnh4sg1ggvyqnh4sg1ggvyq.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158241&t=e3d60d81ac5ada9717efe47778343d37") {
            placeholder(R.mipmap.picture4)
            error(R.mipmap.picture3)
            transformations(CircleCropTransformation())
            scale(Scale.FIT)
        }
    }
}