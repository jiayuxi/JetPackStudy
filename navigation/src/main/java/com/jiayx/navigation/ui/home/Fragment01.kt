package com.jiayx.navigation.ui.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jiayx.navigation.R
import com.jiayx.navigation.databinding.Fragment01Binding
import com.jiayx.navigation.viewmodel.HomeViewModel

/**
 *Created by yuxi_
on 2022/4/19
 */
class Fragment01 : Fragment() {
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    private val binding: Fragment01Binding by lazy {
        Fragment01Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.rotation = viewModel.rotationPosition
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(binding.imageView, "rotation", 0f, 0f)
        animator.duration = 500
        binding.imageView.setOnClickListener {
            if (!animator.isRunning) {
                animator.setFloatValues(
                    binding.imageView.rotation,
                    binding.imageView.rotation + 100
                )
                viewModel.rotationPosition += 100f
                animator.start()
            }
        }

        binding.button.setOnClickListener {
           val args = Fragment01Args.Builder().setUserName("张三").setAge(20).build().toBundle()
            it.findNavController().navigate(R.id.action_fragment01_to_fragment02,args)
        }
        binding.button2.setOnClickListener {
            val args = Fragment01Args.Builder().setUserName("李四").setAge(25).build().toBundle()
            it.findNavController().navigate(R.id.action_fragment01_to_fragment03,args)
        }
    }
}