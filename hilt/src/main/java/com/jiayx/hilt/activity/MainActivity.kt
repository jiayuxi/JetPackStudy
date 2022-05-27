package com.jiayx.hilt.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jiayx.hilt.R
import com.jiayx.hilt.databinding.ActivityMainBinding
import com.jiayx.hilt.di.HiltSimple
import com.jiayx.hilt.viewmodel.HiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var mHiltSimple: HiltSimple

    private val mHiltViewModel: HiltViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mHiltSimple.doSomething()

        mHiltViewModel.mHiltLiveData.observe(this) {
            binding.text.text = it.toString()
        }
    }
}