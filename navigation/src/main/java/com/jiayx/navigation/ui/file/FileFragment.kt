package com.jiayx.navigation.ui.file

import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jiayx.navigation.R
import com.jiayx.navigation.databinding.FileFragmentBinding
import com.jiayx.navigation.viewmodel.FileViewModel

/**
 * 移动
 */
class FileFragment : Fragment() {
    private val viewModel: FileViewModel by viewModels<FileViewModel>()
    private val binding: FileFragmentBinding by lazy {
        FileFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.scaleX = viewModel.scaleFactor
        binding.imageView.scaleY = viewModel.scaleFactor
        val animatorX: ObjectAnimator = ObjectAnimator.ofFloat(binding.imageView, "scaleX", 0f, 0f)
        val animatorY: ObjectAnimator = ObjectAnimator.ofFloat(binding.imageView, "scaleY", 0f, 0f)
        animatorX.duration = 500
        animatorY.duration = 500
        binding.imageView.setOnClickListener {
            if (!animatorX.isRunning) {
                animatorX.setFloatValues(binding.imageView.scaleX + 0.1f)
                animatorY.setFloatValues(binding.imageView.scaleY + 0.1f)
                viewModel.scaleFactor += 0.1f
                animatorX.start()
                animatorY.start()
            }
        }
    }
}