package com.jiayx.flow.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jiayx.flow.databinding.FragmentFlowValueBinding
import com.jiayx.flow.viewmodel.FlowViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/5/14
 */
class FlowValueFragment : Fragment() {
    private val binding: FragmentFlowValueBinding by lazy {
        FragmentFlowValueBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<FlowViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.oneElementFlow.collectLatest {
                    binding.textValue.text = it.toString()
                }
                viewModel.unlimitedElementFlow.collectLatest {
                    binding.textValue2.text = it.toString()
                }
                viewModel.twoFlowValue?.let {
                    it.collectLatest {
                        Log.d("flow_unlimited_log", "twoFlow value :$it ")
                    }
                } ?: kotlin.run { Log.d("flow_unlimited_log", "twoFlow value 为空 ") }
                //todo StateFlow订阅者所在的协程，最好使用独立协程，collect会一直挂起，协程内的后续操作不会执行
                Log.d("state_flow_log", "Flow: 接收数据")
            }
        }

        binding.Button1.setOnClickListener {
            collectValue()
        }
        binding.Button2.setOnClickListener {
            viewModel.cancelJob()
        }
    }

    private fun collectValue() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.unlimitedElementFlow?.collectLatest {
                    binding.textValue3.text = it.toString()
                }
            }
        }
    }
}