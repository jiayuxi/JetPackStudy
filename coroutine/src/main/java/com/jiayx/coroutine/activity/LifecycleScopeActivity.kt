package com.jiayx.coroutine.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/7/9
 */
class LifecycleScopeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       lifecycleScope.launchWhenCreated {

       }
        lifecycleScope.launchWhenStarted {

        }
        lifecycleScope.launchWhenResumed {

        }

        lifecycleScope.launch {
            lifecycle.whenCreated {

            }
            lifecycle.whenStarted {

            }
            lifecycle.whenResumed {

            }
        }
    }
}