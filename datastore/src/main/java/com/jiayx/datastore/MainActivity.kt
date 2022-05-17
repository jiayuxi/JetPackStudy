package com.jiayx.datastore

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jiayx.datastore.databinding.ActivityMainBinding
import com.jiayx.datastore.model.UserModel
import com.jiayx.datastore.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userModel.collectLatest {
                    binding.textView.text = "${it.name} - ${it.email} "
                }
            }
        }
        binding.button.setOnClickListener {
            viewModel.saveData(
                UserModel(
                    binding.name.text.toString(),
                    binding.email.text.toString()
                )
            )
        }
        binding.button2.setOnClickListener {
            viewModel.userModel
        }
    }
}