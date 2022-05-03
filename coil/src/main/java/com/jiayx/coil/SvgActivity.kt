package com.jiayx.coil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jiayx.coil.databinding.ActivityGifBinding
import com.jiayx.coil.databinding.ActivitySvgBinding

/**
 *Created by yuxi_
on 2022/5/3
 */
class SvgActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySvgBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_svg)
        binding.loadImage = R.mipmap.svg
        binding.loadImage2 = R.drawable.ic_baseline_access_alarm_24
        binding.loadImage3 = "https://img-qn-3.51miz.com/2017/05/16/11/2017051611198269_P1177745_8368b2e5.png!/quality/90/unsharp/true/compress/true/format/webp/fh/260"
        binding.placeHolder = R.drawable.picture2
        binding.error = R.drawable.picture1
    }
}