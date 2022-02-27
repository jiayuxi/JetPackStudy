package com.jiayx.jetpackstudy

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.jiayx.jetpackstudy.ui.main.MainFragment
import com.jiayx.jetpackstudy.ui.main.MainViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer


class MainActivity : AppCompatActivity() {

    // 所需的全部权限
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )


    private val permissions2 = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    private fun checkPermissions() {

        val rxPermissions = RxPermissions(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            XXPermissions.with(this) // 不适配 Android 11 可以这样写
                //.permission(Permission.Group.STORAGE)
                // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                .permission(permissions2)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String?>?, all: Boolean) {
                        if (all) {

                        }
                    }

                    override fun onDenied(permissions: List<String?>?, never: Boolean) {
                        if (never) {
                            Toast.makeText(
                                this@MainActivity,
                                "被永久拒绝授权，请手动授予存储权限",
                                Toast.LENGTH_SHORT
                            ).show()
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(
                                this@MainActivity,
                                permissions
                            )
                        } else {
                            Toast.makeText(this@MainActivity, "获取存储权限失败", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
        } else {
        }
    }
}


