package com.jiayx.jetpackstudy

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.jiayx.jetpackstudy.databinding.MainActivityBinding
import com.jiayx.jetpackstudy.ui.main.paging.PagingActivity
import com.jiayx.jetpackstudy.ui.main.paging3.ArticleActivity
import com.jiayx.jetpackstudy.ui.main.paging3.Paging3Activity
import com.jiayx.jetpackstudy.ui.main.paging3.Paging3MediatorActivity
import com.jiayx.jetpackstudy.ui.main.paging3.PokemonActivity
import com.jiayx.jetpackstudy.ui.main.room.RoomActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContentView(binding.root)
        binding.buttonRoom.setOnClickListener {
            startActivity(RoomActivity::class.java)
        }
        binding.buttonPaging.setOnClickListener {
            startActivity(PagingActivity::class.java)
        }
        binding.buttonNetworkPaging.setOnClickListener {
            startActivity(Paging3Activity::class.java)
        }
        binding.buttonMediatorPaging.setOnClickListener {
            startActivity(Paging3MediatorActivity::class.java)
        }
        binding.buttonArticlePaging.setOnClickListener {
            startActivity(ArticleActivity::class.java)
        }
        binding.buttonPokemonPaging.setOnClickListener {
            startActivity(PokemonActivity::class.java)
        }
    }

    private fun <T> startActivity(cls: Class<T>) {
        val intent = Intent(this, cls)
        startActivity(intent)
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


