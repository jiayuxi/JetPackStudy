package com.jiayx.notification

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.jiayx.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction() {
        //创建基本通知
        binding.button1.setOnClickListener {

        }
        if (!checkFloatPermission(this)) {
            Toast.makeText(this, "请给软件设置悬浮窗权限，否则无法正常使用！", Toast.LENGTH_SHORT)
                .show()
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${packageName}")
                )
            )
        }
    }

    private fun checkFloatPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }
}