package com.jiayx.flow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.jiayx.flow.databinding.FragmentStateFlowBinding
import com.jiayx.flow.viewmodel.NumberViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/29
 */
class FlowStateFragment1 : Fragment() {
    private val binding: FragmentStateFlowBinding by lazy {
        FragmentStateFlowBinding.inflate(layoutInflater)
    }
    private val viewModel: NumberViewModel by lazy {
        ViewModelProvider(requireActivity())[NumberViewModel::class.java]
    }

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
        binding.flowStateNumber.text = viewModel.value.toString()
        binding.flowStateIncrement.setOnClickListener {
            viewModel.increment()
        }
        binding.flowStateDecrement.setOnClickListener {
            viewModel.decrement()
        }
        // 可重启生命周期感知型协程
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
                viewModel.number.collect { value ->
//                     binding.flowStateNumber.text = "$value"
                }
            }
        }

        // 方法4
        // 生命周期感知型数据流收集
        viewModel.number.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach { value ->
            binding.flowStateNumber.text = "$value"
        }.launchIn(lifecycleScope)//运行在主线程的协程作用域，在视图销毁时自动取消作用域
        // 方法5
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.number.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { value ->
                    viewModel.value = value
                    binding.flowStateNumber.text = "$value"
                }
        }
    }
}