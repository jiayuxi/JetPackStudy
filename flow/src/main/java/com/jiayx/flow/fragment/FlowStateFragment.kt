package com.jiayx.flow.fragment

import android.os.Bundle
import android.sax.StartElementListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jiayx.flow.databinding.FragmentStateFlowBinding
import com.jiayx.flow.viewmodel.NumberViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
 * on 2022/4/1
 * StateFlow是一个状态容器式可观察数据流，可以向其收集器发出当前状态更新和新状态更新。骇客通过其value属性读取当前状态值。
 *
 * suspend Lifecycle.repeatOnLifecycle 或 Flow.flowWithLifecycle 从 UI 层安全地收集数据流
 */
class FlowStateFragment : Fragment() {
    private val binding: FragmentStateFlowBinding by lazy {
        FragmentStateFlowBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<NumberViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {
        binding.flowStateIncrement.setOnClickListener {
            viewModel.increment()
        }
        binding.flowStateDecrement.setOnClickListener {
            viewModel.decrement()
        }
        // 方法1
        lifecycleScope.launchWhenCreated {
            viewModel.number.collect { value ->
//                binding.flowStateNumber.text = "$value"
            }
        }
        // 方法2
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.number.collect { value ->
//                    binding.flowStateNumber.text = "$value"
                }
            }
        }
        // 方法3
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                 viewModel.number.collect{ value ->
//                     binding.flowStateNumber.text = "$value"
                 }
            }
        }

        // 方法4
        viewModel.number.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach { value ->
            binding.flowStateNumber.text = "$value"
        }.launchIn(lifecycleScope)//运行在主线程的协程作用域，在视图销毁时自动取消作用域
        // 方法5
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.number.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { value ->
                    binding.flowStateNumber.text = "$value"
                }
        }
    }
}