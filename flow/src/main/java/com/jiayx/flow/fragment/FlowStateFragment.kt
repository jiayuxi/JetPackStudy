package com.jiayx.flow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jiayx.flow.databinding.FragmentStateFlowBinding
import com.jiayx.flow.viewmodel.NumberViewModel

/**
 *Created by yuxi_
 * on 2022/4/1
 * StateFlow是一个状态容器式可观察数据流，可以向其收集器发出当前状态更新和新状态更新。骇客通过其value属性读取当前状态值。
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
        lifecycleScope.launchWhenCreated {
            viewModel.number.collect { value ->
                binding.flowStateNumber.text = "$value"
            }
        }
    }
}