package com.jiayx.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jiayx.navigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //TODO 报错代码：
    //Navigation.findNavController(this, R.id.fragmentContainerView2)
    //TODO 错误原因：
    /*使用 FragmentContainerView 创建 NavHostFragment，或通过 FragmentTransaction
     *手动将 NavHostFragment 添加到您的 Activity 时，
     *尝试通过  Navigation.findNavController(Activity, @IdRes int)
     *检索 Activity 的 onCreate() 中的 NavController 将失败
    */
    //todo 解决办法
    /*
    * 1、第一种解决办法
    *  取Navigation的导航控制器 NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

    *  2、第二种解决办法
    *   修改 xml 文件，把 androidx.fragment.app.FragmentContainerView 修改为 fragment
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.fileFragment, R.id.settingFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}