package com.jiayx.navigation.ui.setting

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jiayx.navigation.databinding.SettingFragmentBinding
import com.jiayx.navigation.viewmodel.SettingViewModel
import kotlin.random.Random

/**
 * 移动
 */
class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels<SettingViewModel>()
    private val binding: SettingFragmentBinding by lazy {
        SettingFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.x = binding.imageView.x + viewModel.dx
        val objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(binding.imageView, "x", 0f, 0f)
        objectAnimator.duration = 200
        binding.imageView.setOnClickListener {
            if (!objectAnimator.isRunning) {
                val dx = if (Random.nextBoolean()) 100f else -100f
                objectAnimator.setFloatValues(binding.imageView.x, binding.imageView.x + dx)
                viewModel.dx += dx
                objectAnimator.start()
            }
        }
    }
}