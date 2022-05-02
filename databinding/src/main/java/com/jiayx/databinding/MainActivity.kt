package com.jiayx.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jiayx.databinding.util.EventHandlerListener
import com.jiayx.databinding.bean.User
import com.jiayx.databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val user = User("张三", 20)
        dataBinding.user = user
        dataBinding.eventOnCllick = EventHandlerListener(this)
        dataBinding.networkImage =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.2008php.com%2F09_Website_appreciate%2F10-07-11%2F1278861720_g.jpg&refer=http%3A%2F%2Fwww.2008php.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654010430&t=e5cdc712f1ab52eba269bee9fbeae0e1"
        dataBinding.localImage = R.mipmap.ic_launcher
        dataBinding.errorImage = R.mipmap.ic_launcher
        dataBinding.placeHolderImage = R.mipmap.ic_launcher
    }
}